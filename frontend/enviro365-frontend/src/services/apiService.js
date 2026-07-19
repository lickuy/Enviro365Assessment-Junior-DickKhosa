/**
 * API Service Module
 * 
 * This module provides centralized API communication with the Enviro365 backend.
 * All HTTP requests to the backend go through this service, ensuring consistent
 * error handling, request formatting, and response processing.
 * 
 * Backend URL: http://localhost:8080/api
 * 
 * @author Enviro365 Assessment - Dick Khosa
 * @version 1.0.0
 */

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Centralized API service object containing all backend API methods
 */
const apiService = {
  /**
   * INVESTOR ENDPOINTS
   * ==================
   */

  /**
   * Fetches basic investor information by investor ID
   * 
   * @async
   * @param {number} id - The investor ID to fetch
   * @returns {Promise<Object>} Investor object with id, firstName, lastName, email, etc.
   * @throws {Error} If the investor is not found or API request fails
   * 
   * @example
   * const investor = await apiService.getInvestor(1);
   * console.log(investor.firstName); // "John"
   */
  getInvestor: async (id) => {
    const response = await fetch(`${API_BASE_URL}/investors/${id}`);
    if (!response.ok) throw new Error('Failed to fetch investor');
    return response.json();
  },

  /**
   * Fetches complete investor portfolio including all portfolios and products
   * 
   * This is the main endpoint for the portfolio dashboard. It returns the investor
   * with all nested portfolio data and their products, plus calculated fields like
   * age and retirement eligibility status.
   * 
   * @async
   * @param {number} id - The investor ID to fetch portfolio for
   * @returns {Promise<Object>} InvestorDTO with portfolios and products arrays
   * @throws {Error} If the investor is not found or API request fails
   * 
   * @example
   * const investorWithPortfolio = await apiService.getInvestorPortfolio(1);
   * investorWithPortfolio.portfolios.forEach(portfolio => {
   *   console.log(portfolio.name); // "Retirement Portfolio", etc.
   * });
   */
  getInvestorPortfolio: async (id) => {
    const response = await fetch(`${API_BASE_URL}/investors/${id}/portfolio`);
    if (!response.ok) throw new Error('Failed to fetch portfolio');
    return response.json();
  },

  /**
   * WITHDRAWAL ENDPOINTS
   * ====================
   */

  /**
   * Creates a new withdrawal request for an investor
   * 
   * This endpoint enforces all business rules:
   * - If type is "RETIREMENT", investor must be age >= 65
   * - Withdrawal amount cannot exceed current balance
   * - Withdrawal amount cannot exceed 90% of current balance
   * 
   * @async
   * @param {Object} withdrawalData - Withdrawal request data
   * @param {number} withdrawalData.investorId - ID of the investor
   * @param {number} withdrawalData.amount - Amount to withdraw (in currency units)
   * @param {string} withdrawalData.type - Withdrawal type: "REGULAR" or "RETIREMENT"
   * @returns {Promise<Object>} WithdrawalDTO with id, status, createdAt, balanceAfter, etc.
   * @throws {Error} If business rules are violated or API request fails
   * 
   * @example
   * try {
   *   const withdrawal = await apiService.createWithdrawal({
   *     investorId: 1,
   *     amount: 25000,
   *     type: "RETIREMENT"
   *   });
   *   console.log(withdrawal.status); // "APPROVED"
   * } catch (error) {
   *   console.error(error.message); // "Retirement withdrawals only allowed if age >= 65..."
   * }
   */
  createWithdrawal: async (withdrawalData) => {
    const response = await fetch(`${API_BASE_URL}/withdrawals`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(withdrawalData),
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to create withdrawal');
    }
    return response.json();
  },

  /**
   * Retrieves withdrawal history for a specific investor
   * 
   * Returns all withdrawals for the investor sorted by creation date (newest first)
   * 
   * @async
   * @param {number} investorId - The investor ID to fetch withdrawal history for
   * @returns {Promise<Array>} Array of WithdrawalDTO objects
   * @throws {Error} If investor not found or API request fails
   * 
   * @example
   * const withdrawals = await apiService.getWithdrawalHistory(1);
   * withdrawals.forEach(withdrawal => {
   *   console.log(`${withdrawal.amount} - ${withdrawal.status}`);
   * });
   */
  getWithdrawalHistory: async (investorId) => {
    const response = await fetch(`${API_BASE_URL}/withdrawals?investorId=${investorId}`);
    if (!response.ok) throw new Error('Failed to fetch withdrawal history');
    return response.json();
  },

  /**
   * Exports withdrawal history as a CSV file and triggers download
   * 
   * This function:
   * 1. Fetches withdrawal data as CSV from the backend
   * 2. Creates a blob from the response
   * 3. Generates a download link
   * 4. Triggers automatic download
   * 5. Cleans up resources
   * 
   * @async
   * @param {number} investorId - The investor ID to export withdrawals for
   * @returns {Promise<void>} Does not return data; triggers file download instead
   * @throws {Error} If export fails
   * 
   * @example
   * // User clicks "Export to CSV" button
   * await apiService.exportWithdrawalsCSV(1);
   * // File "withdrawals_1_1689796200000.csv" automatically downloads
   */
  exportWithdrawalsCSV: async (investorId) => {
    const response = await fetch(`${API_BASE_URL}/withdrawals/export?investorId=${investorId}`);
    if (!response.ok) throw new Error('Failed to export CSV');
    
    // Convert response to blob (binary data)
    const blob = await response.blob();
    
    // Create a temporary URL for the blob
    const url = window.URL.createObjectURL(blob);
    
    // Create a temporary download link
    const a = document.createElement('a');
    a.href = url;
    a.download = `withdrawals_${investorId}_${new Date().getTime()}.csv`;
    
    // Trigger download
    document.body.appendChild(a);
    a.click();
    
    // Clean up resources
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
  },
};

export default apiService;
