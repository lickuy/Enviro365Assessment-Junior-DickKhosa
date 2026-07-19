package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private Double totalBalance;
    private Integer age;
    private Boolean isRetirementEligible;
    private List<PortfolioDTO> portfolios;
}
