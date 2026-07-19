/**
 * WithdrawalForm Component
 * 
 * Provides a form for investors to submit withdrawal requests with the following features:
 * - Input validation (amount, type)
 * - Client-side business rule enforcement:
 *   * Age >= 65 for retirement withdrawals
 *   * Amount cannot exceed current balance
 *   * Amount cannot exceed 90% of balance
 * - Error messages and success confirmation
 * - Loading state during submission
 * 
 * The form submits to the backend which performs server-side validation again.
 * 
 * @component
 * @param {Object} props - Component props
 * @param {number} props.investorId - The investor ID submitting the withdrawal
 * @param {Function} props.onWithdrawalSuccess - Callback when withdrawal succeeds
 * @param {number} props.currentBalance - Current investor balance for validation
 * @param {number} props.investorAge - Current investor age for retirement check
 * @param {boolean} props.isRetirementEligible - Whether investor is 65+
 * @returns {JSX.Element} Withdrawal submission form
 * 
 * @author Enviro365 Assessment - Dick Khosa
 * @version 1.0.0
 */

import React, { useState, useEffect } from 'react';
import apiService from '../services/apiService';
import '../styles/WithdrawalForm.css';

function WithdrawalForm({ investorId, onWithdrawalSuccess, currentBalance, investorAge, isRetirementEligible }) {
  // ============================================
  // STATE MANAGEMENT
  // ============================================
  
  /**
   * Form data containing user input
   * @type {[Object, Function]}
   * @property {number} investorId - The investor submitting withdrawal
   * @property {string} amount - Withdrawal amount as string (for input field)
   * @property {string} type - 'REGULAR' or 'RETIREMENT'
   */
  const [formData, setFormData] = useState({
    investorId: investorId,
    amount: '',
    type: 'REGULAR',
  });

  /** @type {[Object, Function]} Form validation errors {fieldName: errorMessage} */
  const [errors, setErrors] = useState({});

  /** @type {[string|null, Function]} API error message if backend rejects withdrawal */
  const [apiError, setApiError] = useState(null);

  /** @type {[boolean, Function]} Success message shown after successful withdrawal */
  const [success, setSuccess] = useState(false);

  /** @type {[boolean, Function]} Loading state while submitting to API */
  const [loading, setLoading] = useState(false);

  // ============================================
  // LIFECYCLE HOOKS
  // ============================================
  
  /**
   * Update investorId in form data when prop changes
   */
  useEffect(() => {
    setFormData(prev => ({ ...prev, investorId: investorId }));
  }, [investorId]);

  // ============================================
  // VALIDATION LOGIC
  // ============================================
  
  /**
   * Validates the withdrawal form against business rules
   * 
   * Business Rules Enforced (CLIENT-SIDE):
   * 1. Amount is provided and positive
   * 2. Amount does not exceed current balance
   * 3. Amount does not exceed 90% of current balance
   * 4. If RETIREMENT type, investor must be age >= 65
   * 
   * Note: Backend also validates these rules
   * 
   * @returns {boolean} True if all validations pass, false otherwise
   */
  const validateForm = () => {
    const newErrors = {};

    // ===== AMOUNT VALIDATION =====
    if (!formData.amount || formData.amount === '') {
      // Amount is required
      newErrors.amount = 'Amount is required';
    } else if (isNaN(formData.amount) || parseFloat(formData.amount) <= 0) {
      // Amount must be positive number
      newErrors.amount = 'Amount must be a positive number';
    } else if (parseFloat(formData.amount) > currentBalance) {
      // Amount cannot exceed available balance
      newErrors.amount = `Cannot exceed balance (R${currentBalance?.toFixed(2)})`;
    } else if (parseFloat(formData.amount) > currentBalance * 0.9) {
      // Amount cannot exceed 90% of balance (business rule)
      const maxAllowed = (currentBalance * 0.9).toFixed(2);
      newErrors.amount = `Cannot exceed 90% of balance (Max: R${maxAllowed})`;
    }

    // ===== RETIREMENT TYPE VALIDATION =====
    if (formData.type === 'RETIREMENT' && !isRetirementEligible) {
      // Only investors aged 65+ can make retirement withdrawals
      newErrors.type = `Retirement withdrawals only allowed for investors aged 65+. Current age: ${investorAge}`;
    }

    setErrors(newErrors);
    // Return true if no errors, false otherwise
    return Object.keys(newErrors).length === 0;
  };

  // ============================================
  // EVENT HANDLERS
  // ============================================
  
  /**
   * Handles input field changes
   * 
   * - Updates formData state
   * - Clears error for the changed field (provides real-time feedback)
   * 
   * @param {React.ChangeEvent<HTMLInputElement|HTMLSelectElement>} e - Change event
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    // Update form data
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    // Clear error message for this field
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  /**
   * Handles form submission
   * 
   * Process:
   * 1. Clear previous error/success messages
   * 2. Validate form data
   * 3. Send to API
   * 4. On success: Show message, clear form, notify parent
   * 5. On error: Show error message to user
   * 
   * @async
   * @param {React.FormEvent<HTMLFormElement>} e - Form submit event
   * @returns {Promise<void>}
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError(null);
    setSuccess(false);

    // Validate form before submitting
    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      
      // Convert to correct types for API
      const withdrawalData = {
        investorId: parseInt(formData.investorId),
        amount: parseFloat(formData.amount),
        type: formData.type,
      };

      // Call backend API
      await apiService.createWithdrawal(withdrawalData);
      
      // Success! Show confirmation message
      setSuccess(true);
      
      // Reset form for next withdrawal
      setFormData({
        investorId: investorId,
        amount: '',
        type: 'REGULAR',
      });

      // Notify parent component (App.js) to refresh other components
      if (onWithdrawalSuccess) {
        onWithdrawalSuccess();
      }

      // Auto-clear success message after 3 seconds
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      // Show error from backend
      setApiError(err.message);
      console.error('Error creating withdrawal:', err);
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // RENDERING
  // ============================================
  
  return (
    <div className="withdrawal-form">
      <h3>Submit Withdrawal</h3>
      
      {/* SUCCESS MESSAGE */}
      {/* Shown when withdrawal is approved */}
      {success && (
        <div className="success-message">
          ✓ Withdrawal approved successfully!
        </div>
      )}

      {/* ERROR MESSAGE FROM API */}
      {/* Shown when backend rejects withdrawal (business rule violation) */}
      {apiError && (
        <div className="error-message">
          ✗ {apiError}
        </div>
      )}

      {/* FORM */}
      <form onSubmit={handleSubmit}>
        
        {/* AMOUNT FIELD */}
        <div className="form-group">
          <label htmlFor="amount">Withdrawal Amount (R):</label>
          <input
            type="number"
            id="amount"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            placeholder="Enter amount"
            step="0.01"
            min="0"
          />
          {/* Show validation error if present */}
          {errors.amount && <span className="error">{errors.amount}</span>}
          {/* Show maximum allowed amount (90% of balance) */}
          <small>Max allowed: R{(currentBalance * 0.9).toFixed(2)} (90%)</small>
        </div>

        {/* WITHDRAWAL TYPE FIELD */}
        <div className="form-group">
          <label htmlFor="type">Withdrawal Type:</label>
          <select
            id="type"
            name="type"
            value={formData.type}
            onChange={handleChange}
          >
            <option value="REGULAR">Regular Withdrawal</option>
            <option value="RETIREMENT">Retirement Withdrawal</option>
          </select>
          {/* Show validation error if present */}
          {errors.type && <span className="error">{errors.type}</span>}
          {/* Show retirement eligibility status */}
          {formData.type === 'RETIREMENT' && (
            <small>
              {isRetirementEligible 
                ? '✓ You are eligible for retirement withdrawals' 
                : '✗ You must be 65+ for retirement withdrawals'}
            </small>
          )}
        </div>

        {/* SUBMIT BUTTON */}
        <button type="submit" disabled={loading}>
          {loading ? 'Processing...' : 'Submit Withdrawal'}
        </button>
      </form>
    </div>
  );
}

export default WithdrawalForm;