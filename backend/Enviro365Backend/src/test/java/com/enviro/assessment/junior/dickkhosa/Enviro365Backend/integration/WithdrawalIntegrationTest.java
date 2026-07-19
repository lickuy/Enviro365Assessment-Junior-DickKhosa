package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.integration;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.controller.WithdrawalController;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Investor;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.InvestorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test demonstrating full backend-frontend communication flow
 */
@SpringBootTest
@ActiveProfiles("test")
public class WithdrawalIntegrationTest {

    @Autowired
    private WithdrawalController withdrawalController;

    @Autowired
    private InvestorRepository investorRepository;

    private Long testInvestorId;

    @BeforeEach
    public void setUp() {
        // Ensure clean DB for each test
        investorRepository.deleteAll();
        // Create test investor (retirement-eligible placeholder)
        Investor investor = new Investor();
        investor.setFirstName("Integration");
        investor.setLastName("Test");
        investor.setEmail("integration@test.com");
        investor.setDateOfBirth(LocalDate.of(1956, 1, 1));
        investor.setPhoneNumber("0123456789");
        investor.setTotalBalance(100000.0);

        Investor saved = investorRepository.save(investor);
        testInvestorId = saved.getId();
    }

    @Test
    public void testFullWithdrawalFlow() {
        // Step 1: Create a valid retirement withdrawal (age 70, eligible)
        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(testInvestorId);
        request.setAmount(50000.0); // Within 90% limit
        request.setType("RETIREMENT");

        ResponseEntity<?> response = withdrawalController.createWithdrawal(request);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testWithdrawalHistoryRetrieval() {
        // Create a withdrawal first
        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(testInvestorId);
        request.setAmount(25000.0);
        request.setType("REGULAR");

        withdrawalController.createWithdrawal(request);

        // Retrieve history
        ResponseEntity<?> response = withdrawalController.getWithdrawalHistory(testInvestorId);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testInvalidWithdrawalRejection() {
        // Try to withdraw more than 90%
        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(testInvestorId);
        request.setAmount(95000.0); // 95% > 90% limit
        request.setType("REGULAR");

        assertThrows(Exception.class, () -> {
            withdrawalController.createWithdrawal(request);
        });
    }

    @Test
    public void testCSVExport() {
        // Create a withdrawal
        WithdrawalRequestDTO request = new WithdrawalRequestDTO();
        request.setInvestorId(testInvestorId);
        request.setAmount(10000.0);
        request.setType("REGULAR");

        withdrawalController.createWithdrawal(request);

        // Export to CSV
        ResponseEntity<String> response = (ResponseEntity<String>) withdrawalController.exportWithdrawalsToCSV(testInvestorId);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("ID,Investor ID,Amount,Type,Status"));
    }
}
