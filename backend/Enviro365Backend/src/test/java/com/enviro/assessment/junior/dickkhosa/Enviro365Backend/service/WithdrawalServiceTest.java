package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.service;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Investor;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Withdrawal;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.exception.InvalidWithdrawalException;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.mapper.WithdrawalMapper;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.InvestorRepository;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.WithdrawalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawalServiceTest {

    @Mock
    private WithdrawalRepository withdrawalRepository;

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private WithdrawalMapper withdrawalMapper;

    @InjectMocks
    private WithdrawalService withdrawalService;

    private Investor retiredInvestor;
    private Investor youngInvestor;
    private WithdrawalRequestDTO withdrawalRequest;

    @BeforeEach
    public void setUp() {
        // Investor over 65 (retirement eligible)
        retiredInvestor = new Investor();
        retiredInvestor.setId(1L);
        retiredInvestor.setFirstName("John");
        retiredInvestor.setDateOfBirth(LocalDate.of(1955, 5, 15));
        retiredInvestor.setTotalBalance(100000.0);

        // Investor under 65 (not retirement eligible)
        youngInvestor = new Investor();
        youngInvestor.setId(2L);
        youngInvestor.setFirstName("Jane");
        youngInvestor.setDateOfBirth(LocalDate.of(1990, 3, 22));
        youngInvestor.setTotalBalance(50000.0);

        withdrawalRequest = new WithdrawalRequestDTO();
        withdrawalRequest.setInvestorId(1L);
        withdrawalRequest.setAmount(20000.0);
        withdrawalRequest.setType("RETIREMENT");
    }

    @Test
    public void testCreateWithdrawal_Success() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(retiredInvestor));
        when(withdrawalRepository.save(any(Withdrawal.class))).thenAnswer(i -> i.getArgument(0));
        when(investorRepository.save(any(Investor.class))).thenAnswer(i -> i.getArgument(0));

        Withdrawal savedWithdrawal = new Withdrawal();
        savedWithdrawal.setId(1L);
        savedWithdrawal.setAmount(20000.0);
        when(withdrawalMapper.toDTO(any(Withdrawal.class))).thenReturn(new WithdrawalDTO());

        WithdrawalDTO result = withdrawalService.createWithdrawal(withdrawalRequest);

        assertNotNull(result);
        verify(withdrawalRepository, times(1)).save(any(Withdrawal.class));
    }

    @Test
    public void testCreateWithdrawal_RetirementEligibilityFailed() {
        withdrawalRequest.setInvestorId(2L);
        withdrawalRequest.setType("RETIREMENT");

        when(investorRepository.findById(2L)).thenReturn(Optional.of(youngInvestor));

        assertThrows(InvalidWithdrawalException.class, () -> {
            withdrawalService.createWithdrawal(withdrawalRequest);
        });

        verify(withdrawalRepository, never()).save(any(Withdrawal.class));
    }

    @Test
    public void testCreateWithdrawal_ExceedsBalance() {
        withdrawalRequest.setAmount(150000.0); // More than available balance

        when(investorRepository.findById(1L)).thenReturn(Optional.of(retiredInvestor));

        assertThrows(InvalidWithdrawalException.class, () -> {
            withdrawalService.createWithdrawal(withdrawalRequest);
        });

        verify(withdrawalRepository, never()).save(any(Withdrawal.class));
    }

    @Test
    public void testCreateWithdrawal_Exceeds90Percent() {
        withdrawalRequest.setAmount(95000.0); // More than 90% of balance

        when(investorRepository.findById(1L)).thenReturn(Optional.of(retiredInvestor));

        assertThrows(InvalidWithdrawalException.class, () -> {
            withdrawalService.createWithdrawal(withdrawalRequest);
        });

        verify(withdrawalRepository, never()).save(any(Withdrawal.class));
    }

    @Test
    public void testCreateWithdrawal_MaximumAllowed() {
        withdrawalRequest.setAmount(90000.0); // Exactly 90% of balance

        when(investorRepository.findById(1L)).thenReturn(Optional.of(retiredInvestor));
        when(withdrawalRepository.save(any(Withdrawal.class))).thenAnswer(i -> i.getArgument(0));
        when(investorRepository.save(any(Investor.class))).thenAnswer(i -> i.getArgument(0));
        when(withdrawalMapper.toDTO(any(Withdrawal.class))).thenReturn(new WithdrawalDTO());

        WithdrawalDTO result = withdrawalService.createWithdrawal(withdrawalRequest);

        assertNotNull(result);
        verify(withdrawalRepository, times(1)).save(any(Withdrawal.class));
    }

    @Test
    public void testCreateWithdrawal_InvestorNotFound() {
        when(investorRepository.findById(999L)).thenReturn(Optional.empty());

        withdrawalRequest.setInvestorId(999L);

        assertThrows(ResourceNotFoundException.class, () -> {
            withdrawalService.createWithdrawal(withdrawalRequest);
        });

        verify(withdrawalRepository, never()).save(any(Withdrawal.class));
    }
}
