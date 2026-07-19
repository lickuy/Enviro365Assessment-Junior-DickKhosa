/**
 * Main Application Component
 * 
 * This is the root component of the Enviro365 Assessment application.
 * 
 * Responsibilities:
 * 1. Manage global state (investor data, refresh triggers)
 * 2. Fetch and maintain investor information
 * 3. Compose all major components (Portfolio, Withdrawal Form, History)
 * 4. Handle inter-component communication and data refresh
 * 5. Provide overall application layout and styling
 * 
 * Data Flow:
 * App.js (State) → Child Components (Display)
 *                ↑
 *                └─ Child Components (Callbacks)
 * 
 * When a user submits a withdrawal:
 * 1. WithdrawalForm calls onWithdrawalSuccess callback
 * 2. App.js increments refreshTrigger
 * 3. WithdrawalHistory detects change and refetches data
 * 4. App.js refetches investor data (to update balance)
 * 
 * @component
 * @returns {JSX.Element} Complete application UI
 * 
 * @author Enviro365 Assessment - Dick Khosa
 * @version 1.0.0
 */

import React, { useState, useEffect } from 'react';
import './App.css';
import PortfolioDashboard from './components/PortfolioDashboard';
import WithdrawalForm from './components/WithdrawalForm';
import WithdrawalHistory from './components/WithdrawalHistory';
import apiService from './services/apiService';

function App() {
  // ============================================
  // CONFIGURATION
  // ============================================
  
  /**
   * Default investor ID for demo application
   * This can be changed to 2 or 3 to test with different investors:
   * - 1: John Doe (age 69, retirement eligible)
   * - 2: Jane Smith (age 36, NOT eligible)
   * - 3: Robert Johnson (age 81, retirement eligible)
   * @constant {number}
   */
  const INVESTOR_ID = 1;

  // ============================================
  // STATE MANAGEMENT
  // ============================================
  
  /**
   * Current investor data fetched from backend
   * Contains investor details and balance
   * @type {[Object|null, Function]}
   */
  const [investor, setInvestor] = useState(null);

  /**
   * Loading state while fetching investor data
   * @type {[boolean, Function]}
   */
  const [loading, setLoading] = useState(true);

  /**
   * Error message if investor data fetch fails
   * @type {[string|null, Function]}
   */
  const [error, setError] = useState(null);

  /**
   * Refresh trigger for child components
   * 
   * This counter is incremented when a withdrawal succeeds,
   * which signals WithdrawalHistory to refetch its data.
   * It's a simple way to trigger component updates without
   * using context or state management library.
   * 
   * @type {[number, Function]}
   */
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  // ============================================
  // LIFECYCLE HOOKS
  // ============================================
  
  /**
   * Fetch investor data when component mounts
   * 
   * This runs once on initial load to populate
   * the investor information, balance, and eligibility status
   */
  useEffect(() => {
    fetchInvestor();
  }, []);

  // ============================================
  // API METHODS
  // ============================================
  
  /**
   * Fetches investor data from backend
   * 
   * Retrieves:
   * - Investor details (name, email, age)
   * - Current balance
   * - Retirement eligibility (calculated on backend)
   * 
   * This data is passed to child components for:
   * - Displaying in portfolio dashboard
   * - Validating withdrawals in form
   * - Showing in balance display
   * 
   * @async
   * @returns {Promise<void>}
   */
  const fetchInvestor = async () => {
    try {
      setLoading(true);
      const data = await apiService.getInvestor(INVESTOR_ID);
      setInvestor(data);
      setError(null);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching investor:', err);
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // EVENT HANDLERS (Callbacks for Child Components)
  // ============================================
  
  /**
   * Callback invoked when a withdrawal is successfully submitted
   * 
   * This function:
   * 1. Increments refreshTrigger to signal WithdrawalHistory to refresh
   * 2. Calls fetchInvestor to update balance after withdrawal
   * 
   * By incrementing refreshTrigger, WithdrawalHistory's useEffect
   * dependency array is triggered, causing it to refetch withdrawal history
   * 
   * @returns {void}
   */
  const handleWithdrawalSuccess = () => {
    // Trigger refresh of withdrawal history and investor data
    setRefreshTrigger(prev => prev + 1);
    fetchInvestor();
  };

  // ============================================
  // RENDERING
  // ============================================
  
  // Show loading state while fetching initial data
  if (loading) {
    return <div className="App"><div className="loading">Loading...</div></div>;
  }

  // Show error state if fetch failed
  if (error) {
    return <div className="App"><div className="error">Error: {error}</div></div>;
  }

  // Main render: Application with all sections
  return (
    <div className="App">
      {/* 
        APPLICATION HEADER
        ==================
        Title and description of the application
      */}
      <header className="app-header">
        <h1>Enviro365 - Investor Portfolio Management</h1>
        <p>Manage your investments and withdrawals</p>
      </header>

      {/* 
        MAIN APPLICATION CONTAINER
        ==========================
        Contains all major components
      */}
      <main className="app-container">
        {investor ? (
          <>
            {/* 
              PORTFOLIO SECTION
              =================
              Displays investor portfolio, holdings, and balance
            */}
            <section className="section">
              <PortfolioDashboard investorId={INVESTOR_ID} />
            </section>

            {/* 
              WITHDRAWAL FORM SECTION
              =======================
              Form to submit new withdrawal requests
              
              Props:
              - investorId: Which investor is withdrawing
              - onWithdrawalSuccess: Callback when withdrawal succeeds
              - currentBalance: For form validation
              - investorAge: For retirement eligibility check
              - isRetirementEligible: Whether investor can make retirement withdrawals
            */}
            <section className="section">
              <WithdrawalForm 
                investorId={INVESTOR_ID}
                onWithdrawalSuccess={handleWithdrawalSuccess}
                currentBalance={investor.totalBalance}
                investorAge={investor.age}
                isRetirementEligible={investor.isRetirementEligible}
              />
            </section>

            {/* 
              WITHDRAWAL HISTORY SECTION
              ==========================
              Table of all past withdrawals
              
              Props:
              - investorId: Which investor's history to show
              - refreshTrigger: Signals when to refetch data
                 (incremented after successful withdrawal)
            */}
            <section className="section">
              <WithdrawalHistory 
                investorId={INVESTOR_ID}
                refreshTrigger={refreshTrigger}
              />
            </section>
          </>
        ) : (
          // Fallback if no investor data
          <div className="error">No investor data available</div>
        )}
      </main>

      {/* 
        APPLICATION FOOTER
        ==================
        Copyright and company information
      */}
      <footer className="app-footer">
        <p>&copy; 2026 Enviro365 Investments. All rights reserved.</p>
      </footer>
    </div>
  );
}

export default App;