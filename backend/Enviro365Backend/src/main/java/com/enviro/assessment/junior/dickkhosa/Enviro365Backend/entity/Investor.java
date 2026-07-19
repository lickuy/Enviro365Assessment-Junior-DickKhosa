package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "investors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Double totalBalance;

    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Portfolio> portfolios;

    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Withdrawal> withdrawals;

    public int getAge() {
        // Use Period to correctly account for month/day when calculating age
        return java.time.Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public boolean isRetirementEligible() {
        return getAge() >= 65;
    }
}
