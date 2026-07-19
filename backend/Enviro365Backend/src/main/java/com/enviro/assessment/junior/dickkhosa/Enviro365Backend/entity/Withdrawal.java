package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "withdrawals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type; // "RETIREMENT" or "REGULAR"

    @Column(nullable = false)
    private String status; // "APPROVED", "REJECTED", "PENDING"

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 500)
    private String rejectionReason;

    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private Double balanceAfter;
}
