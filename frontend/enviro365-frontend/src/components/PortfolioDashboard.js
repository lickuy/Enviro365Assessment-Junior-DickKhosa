/**
 * PortfolioDashboard Component
 * 
 * Displays the investor's portfolio information including:
 * - Investor details (name, age, email, total balance)
 * - Retirement eligibility status
 * - All portfolios with products breakdown
 * 
 * This component fetches data from the backend when mounted and handles
 * loading states, errors, and rendering of nested portfolio/product data.
 * 
 * @component
 * @param {Object} props - Component props
 * @param {number} props.investorId - The ID of the investor to display portfolio for
 * @returns {JSX.Element} Portfolio dashboard UI
 * 
 * @author Enviro365 Assessment - Dick Khosa
 * @version 1.0.0
 * 
 * @example
 * // Render portfolio for investor ID 1
 * <PortfolioDashboard investorId={1} />
 */

import React, { useState, useEffect } from 'react';
import apiService from '../services/apiService';
import '../styles/PortfolioDashboard.css';

function PortfolioDashboard({ investorId }) {
  // ============================================
  // STATE MANAGEMENT
  // ============================================
  
  /** @type {[Object|null, Function]} Investor data including portfolios and products */
  const [investor, setInvestor] = useState(null);

  /** @type {[boolean, Function]} Loading state while fetching data from backend */
  const [loading, setLoading] = useState(true);

  /** @type {[string|null, Function]} Error message if API call fails */
  const [error, setError] = useState(null);

  // ============================================
  // LIFECYCLE HOOKS
  // ============================================
  
  /**
   * Fetch investor portfolio when component mounts or investorId changes
   * 
   * This effect:
   * 1. Sets loading to true
   * 2. Calls API to fetch investor portfolio with portfolios and products
   * 3. Updates component state with fetched data
   * 4. Clears any previous error messages
   * 5. Sets loading to false when complete
   */
  useEffect(() => {
    fetchInvestorPortfolio();
  }, [investorId]); // Re-fetch if investorId prop changes

  // ============================================
  // API METHODS
  // ============================================
  
  /**
   * Fetches investor portfolio data from backend API
   * 
   * Calls GET /api/investors/{id}/portfolio which returns:
   * - Investor basic info (name, age, email, balance)
   * - Array of portfolios
   * - Array of products within each portfolio
   * - Calculated fields (age, retirement eligibility)
   * 
   * @async
   * @returns {Promise<void>}
   * @throws {Error} Logs error to console if fetch fails
   */
  const fetchInvestorPortfolio = async () => {
    try {
      setLoading(true);
      const data = await apiService.getInvestorPortfolio(investorId);
      setInvestor(data);
      setError(null);
    } catch (err) {
      setError(err.message);
      console.error('Error fetching portfolio:', err);
    } finally {
      setLoading(false);
    }
  };
  // ============================================
  // RENDERING LOGIC
  // ============================================
  
  // Show loading state while fetching data
  if (loading) return <div className="loading">Loading portfolio...</div>;
  
  // Show error state if API call failed
  if (error) return <div className="error">Error: {error}</div>;
  
  // Show error if no investor data available
  if (!investor) return <div className="error">No investor data found</div>;

  // Main render: Display portfolio dashboard
  return (
    <div className="portfolio-dashboard">
      <h2>Investor Portfolio</h2>
      
      {/* 
        INVESTOR INFORMATION SECTION
        =============================
        Displays basic investor details:
        - Name, email, age
        - Total balance in R (South African Rand)
        - Retirement eligibility status (calculated on backend)
      */}
      <div className="investor-info">
        <h3>{investor.firstName} {investor.lastName}</h3>
        <p><strong>Email:</strong> {investor.email}</p>
        <p><strong>Age:</strong> {investor.age}</p>
        <p><strong>Total Balance:</strong> R{investor.totalBalance?.toFixed(2)}</p>
        <p>
          <strong>Retirement Eligible:</strong> 
          {/* 
            Retirement eligibility is determined by age >= 65
            This affects whether investor can make retirement withdrawals
          */}
          <span className={investor.isRetirementEligible ? 'eligible' : 'not-eligible'}>
            {investor.isRetirementEligible ? 'Yes' : 'No'}
          </span>
        </p>
      </div>

      {/* 
        PORTFOLIOS SECTION
        ==================
        Displays all investment portfolios for this investor.
        Each portfolio contains products (stocks, bonds, etc.)
        
        Portfolio structure:
        - Portfolio 1 (e.g., "Retirement Portfolio")
          - Product A (e.g., "Stock Fund")
          - Product B (e.g., "Bond Fund")
        - Portfolio 2 (e.g., "Growth Portfolio")
          - Product C
          - Product D
      */}
      {investor.portfolios && investor.portfolios.length > 0 ? (
        <div className="portfolios-container">
          {/* 
            Map through each portfolio and create a card
            Each card shows portfolio name, balance, and its products
          */}
          {investor.portfolios.map((portfolio) => (
            <div key={portfolio.id} className="portfolio-card">
              <h4>{portfolio.name}</h4>
              <p><strong>Balance:</strong> R{portfolio.balance?.toFixed(2)}</p>
              
              {/* 
                PRODUCTS SECTION
                =================
                Shows individual products/holdings within each portfolio
                Each product has: name, type (EQUITY/BONDS/etc), value, unit price
              */}
              {portfolio.products && portfolio.products.length > 0 && (
                <div className="products">
                  <h5>Products:</h5>
                  <ul>
                    {/* 
                      Render each product with its details
                      - Name and type (e.g., "Stock Fund (EQUITY)")
                      - Total value and unit price
                    */}
                    {portfolio.products.map((product) => (
                      <li key={product.id}>
                        <strong>{product.name}</strong> ({product.type})
                        <br />Value: R{product.value?.toFixed(2)} | Unit Price: R{product.unitPrice?.toFixed(2)}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          ))}
        </div>
      ) : (
        // Fallback message if no portfolios exist
        <p>No portfolios found</p>
      )}
    </div>
  );
}

export default PortfolioDashboard;
