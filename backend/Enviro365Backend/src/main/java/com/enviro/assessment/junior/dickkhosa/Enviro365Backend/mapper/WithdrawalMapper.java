package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.mapper;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Withdrawal;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalMapper {
    public WithdrawalDTO toDTO(Withdrawal withdrawal) {
        if (withdrawal == null) {
            return null;
        }

        WithdrawalDTO dto = new WithdrawalDTO();
        dto.setId(withdrawal.getId());
        dto.setInvestorId(withdrawal.getInvestor().getId());
        dto.setAmount(withdrawal.getAmount());
        dto.setType(withdrawal.getType());
        dto.setStatus(withdrawal.getStatus());
        dto.setCreatedAt(withdrawal.getCreatedAt());
        dto.setRejectionReason(withdrawal.getRejectionReason());
        dto.setApprovedAt(withdrawal.getApprovedAt());
        dto.setBalanceAfter(withdrawal.getBalanceAfter());
        return dto;
    }

    public Withdrawal toEntity(WithdrawalDTO dto) {
        if (dto == null) {
            return null;
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(dto.getId());
        withdrawal.setAmount(dto.getAmount());
        withdrawal.setType(dto.getType());
        withdrawal.setStatus(dto.getStatus());
        withdrawal.setCreatedAt(dto.getCreatedAt());
        withdrawal.setRejectionReason(dto.getRejectionReason());
        withdrawal.setApprovedAt(dto.getApprovedAt());
        withdrawal.setBalanceAfter(dto.getBalanceAfter());
        return withdrawal;
    }
}
