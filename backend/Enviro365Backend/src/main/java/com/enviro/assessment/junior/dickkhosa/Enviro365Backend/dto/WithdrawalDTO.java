package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalDTO {
    private Long id;
    private Long investorId;
    private Double amount;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private String rejectionReason;
    private LocalDateTime approvedAt;
    private Double balanceAfter;
}
