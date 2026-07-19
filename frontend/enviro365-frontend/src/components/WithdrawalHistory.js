/**
 * WithdrawalHistory Component
 * 
 * Displays a table of all withdrawal transactions for an investor with:
 * - Complete withdrawal details (amount, type, status, dates, balance)
 * - Status badges (APPROVED, REJECTED, PENDING)
 * - CSV export functionality
 * - Auto-refresh when new withdrawals are created
 * 
 * This component reads from a "refreshTrigger" prop to detect when new
 * withdrawals have been submitted and automatically updates the history.
 * 
 * @component
 * @param {Object} props - Component props
 * @param {number} props.investorId - The investor ID to fetch history for
 * @param {number} props.refreshTrigger - Counter that increments when data should refresh
 * @returns {JSX.Element} Withdrawal history table UI
 * 
 * @author Enviro365 Assessment - Dick Khosa
 * @version 1.0.0
 * 
 * @example
 * // Component re-fetches when refreshTrigger changes
 * <WithdrawalHistory investorId={1} refreshTrigger={refreshCount} />
 */

import React, { useState, useEffect } from 'react';
import apiService from '../services/apiService';
import '../styles/WithdrawalHistory.css';

function WithdrawalHistory({ investorId, refreshTrigger }) {
  // ============================================
  // STATE MANAGEMENT
  // ============================================
  
  /**
   * Array of withdrawal records for the investor
   * @type {[Array<Object>, Function]}
   * Each withdrawal contains: id, investorId, amount, type, status, createdAt, etc.
   */
  const [withdrawals, setWithdrawals] = useState([]);

  /** @type {[boolean, Function]} Loading state while fetching from API */
  const [loading, setLoading] = useState(true);

  /** @type {[string|null, Function]} Error message if API call fails */
  const [error, setError] = useState(null);

  // ============================================
  // LIFECYCLE HOOKS
  // ============================================
  
  /**
   * Fetch withdrawal history when component mounts or dependencies change
   * 
   * Dependencies:
   * - investorId: Refetch when investor changes
   * - refreshTrigger: Refetch when parent component signals update
   *   (e.g., after successful withdrawal submission)
   */
  useEffect(() => {
    fetchWithdrawalHistory();
  }, [investorId, refreshTrigger]);

  // ============================================
  // API METHODS
  // ============================================
  
  /**
   * Fetches all withdrawals for the investor from backend
   * 
   * Calls GET /api/withdrawals?investorId={id}
   * Returns withdrawals sorted by creation date (newest first)
   * 
   * @async
   * @returns {Promise<void>}
   * @throws {Error} Logs error to console if fetch fails
   */
  const fetchWithdrawalHistory = async () => {
    try {
      setLoading(true);
      const data = await apiService.getWithdrawalHistory(investorId);
      setWithdrawals(data);
      setError(null);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching withdrawal history:', err);
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // EXPORT HANDLER
  // ============================================
  
  /**
   * Exports withdrawal history as CSV file
   * 
   * Triggers download of CSV file containing all withdrawal records
   * Filename includes investorId and timestamp for uniqueness
   * 
   * @async
   * @returns {Promise<void>}
   */
  const handleExportCSV = async () => {
    try {
      await apiService.exportWithdrawalsCSV(investorId);
    } catch (err) {
      // Show error if export fails
      setError('Failed to export CSV: ' + err.message);
    }
  };

  // ============================================
  // RENDERING
  // ============================================
  
  // Show loading state while fetching data
  if (loading) return <div className="loading">Loading withdrawal history...</div>;

  return (
    <div className="withdrawal-history">
      
      {/* HEADER WITH TITLE AND EXPORT BUTTON */}
      <div className="history-header">
        <h3>Withdrawal History</h3>
        {/* CSV Export button - downloads all withdrawals as spreadsheet */}
        <button onClick={handleExportCSV} className="export-btn">
          📥 Export to CSV
        </button>
      </div>

      {/* ERROR MESSAGE */}
      {/* Shown if API call fails or export fails */}
      {error && <div className="error-message">Error: {error}</div>}

      {/* WITHDRAWAL TABLE */}
      {/* Shows all withdrawals in tabular format */}
      {withdrawals && withdrawals.length > 0 ? (
        <table className="history-table">
          {/* TABLE HEADER */}
          <thead>
            <tr>
              <th>ID</th>
              <th>Amount (R)</th>
              <th>Type</th>
              <th>Status</th>
              <th>Created Date</th>
              <th>Approved Date</th>
              <th>Balance After (R)</th>
            </tr>
          </thead>
          
          {/* TABLE BODY */}
          {/* Each row represents one withdrawal transaction */}
          <tbody>
            {withdrawals.map((withdrawal) => (
              <tr key={withdrawal.id} className={`status-${withdrawal.status.toLowerCase()}`}>
                {/* Withdrawal ID */}
                <td>{withdrawal.id}</td>
                
                {/* Amount withdrawn (formatted to 2 decimal places) */}
                <td>{withdrawal.amount?.toFixed(2)}</td>
                
                {/* Type of withdrawal (REGULAR or RETIREMENT) */}
                <td>{withdrawal.type}</td>
                
                {/* Status badge (APPROVED, REJECTED, PENDING) */}
                {/* Different colors for different statuses */}
                <td>
                  <span className={`status-badge ${withdrawal.status.toLowerCase()}`}>
                    {withdrawal.status}
                  </span>
                </td>
                
                {/* Date withdrawal was created/submitted */}
                <td>{new Date(withdrawal.createdAt).toLocaleString()}</td>
                
                {/* Date withdrawal was approved (empty if rejected/pending) */}
                <td>{withdrawal.approvedAt ? new Date(withdrawal.approvedAt).toLocaleString() : '-'}</td>
                
                {/* Balance remaining after this withdrawal */}
                <td>{withdrawal.balanceAfter?.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        /* Fallback message when no withdrawals exist */
        <p className="no-data">No withdrawals found</p>
      )}
    </div>
  );
}

export default WithdrawalHistory;