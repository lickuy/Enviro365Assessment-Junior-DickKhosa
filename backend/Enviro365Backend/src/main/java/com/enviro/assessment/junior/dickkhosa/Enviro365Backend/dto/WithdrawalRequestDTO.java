package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestDTO {
    @NotNull(message = "Investor ID is required")
    private Long investorId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Withdrawal type is required")
    private String type; // "RETIREMENT" or "REGULAR"
}
