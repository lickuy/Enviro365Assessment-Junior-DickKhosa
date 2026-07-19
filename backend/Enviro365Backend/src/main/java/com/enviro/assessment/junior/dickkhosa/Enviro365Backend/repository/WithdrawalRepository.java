package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    List<Withdrawal> findByInvestorId(Long investorId);
    List<Withdrawal> findByInvestorIdOrderByCreatedAtDesc(Long investorId);
}
