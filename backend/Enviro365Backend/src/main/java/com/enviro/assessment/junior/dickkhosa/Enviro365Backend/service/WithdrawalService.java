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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private WithdrawalMapper withdrawalMapper;

    public WithdrawalDTO createWithdrawal(WithdrawalRequestDTO request) {
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + request.getInvestorId()));

        // Business rule validation
        validateWithdrawal(investor, request);

        // Create withdrawal record
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setInvestor(investor);
        withdrawal.setAmount(request.getAmount());
        withdrawal.setType(request.getType());
        withdrawal.setCreatedAt(LocalDateTime.now());
        withdrawal.setBalanceAfter(investor.getTotalBalance() - request.getAmount());
        withdrawal.setStatus("APPROVED");
        withdrawal.setApprovedAt(LocalDateTime.now());

        // Update investor balance
        investor.setTotalBalance(investor.getTotalBalance() - request.getAmount());
        investorRepository.save(investor);

        Withdrawal savedWithdrawal = withdrawalRepository.save(withdrawal);
        return withdrawalMapper.toDTO(savedWithdrawal);
    }

    private void validateWithdrawal(Investor investor, WithdrawalRequestDTO request) {
        Double withdrawalAmount = request.getAmount();
        Double currentBalance = investor.getTotalBalance();
        String withdrawalType = request.getType();

        // Check if it's a retirement withdrawal
        if ("RETIREMENT".equalsIgnoreCase(withdrawalType)) {
            // Rule 1: Only allowed if age >= 65
            if (!investor.isRetirementEligible()) {
                throw new InvalidWithdrawalException(
                        "Retirement withdrawals only allowed if age >= 65. Current age: " + investor.getAge()
                );
            }
        }

        // Rule 2: Withdrawal must not exceed balance
        if (withdrawalAmount > currentBalance) {
            throw new InvalidWithdrawalException(
                    "Withdrawal amount cannot exceed current balance. Requested: " + withdrawalAmount
                            + ", Available: " + currentBalance
            );
        }

        // Rule 3: Withdrawal must not exceed 90% of balance
        Double maxAllowedWithdrawal = currentBalance * 0.9;
        if (withdrawalAmount > maxAllowedWithdrawal) {
            throw new InvalidWithdrawalException(
                    "Withdrawal amount cannot exceed 90% of balance. Requested: " + withdrawalAmount
                            + ", Maximum allowed: " + maxAllowedWithdrawal
            );
        }
    }

    public List<WithdrawalDTO> getWithdrawalHistory(Long investorId) {
        // Verify investor exists
        investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));

        List<Withdrawal> withdrawals = withdrawalRepository.findByInvestorIdOrderByCreatedAtDesc(investorId);
        return withdrawals.stream()
                .map(withdrawalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WithdrawalDTO getWithdrawalById(Long withdrawalId) {
        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal not found with id: " + withdrawalId));
        return withdrawalMapper.toDTO(withdrawal);
    }
}
