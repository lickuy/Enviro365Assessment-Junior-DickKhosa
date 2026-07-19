package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.controller;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
@CrossOrigin(origins = "http://localhost:3000")
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;

    @PostMapping
    public ResponseEntity<WithdrawalDTO> createWithdrawal(@Valid @RequestBody WithdrawalRequestDTO request) {
        WithdrawalDTO withdrawal = withdrawalService.createWithdrawal(request);
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping
    public ResponseEntity<List<WithdrawalDTO>> getWithdrawalHistory(@RequestParam Long investorId) {
        List<WithdrawalDTO> withdrawals = withdrawalService.getWithdrawalHistory(investorId);
        return ResponseEntity.ok(withdrawals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WithdrawalDTO> getWithdrawal(@PathVariable Long id) {
        WithdrawalDTO withdrawal = withdrawalService.getWithdrawalById(id);
        return ResponseEntity.ok(withdrawal);
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportWithdrawalsToCSV(@RequestParam Long investorId) {
        List<WithdrawalDTO> withdrawals = withdrawalService.getWithdrawalHistory(investorId);
        String csv = generateCSV(withdrawals);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"withdrawals.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv);
    }

    private String generateCSV(List<WithdrawalDTO> withdrawals) {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Investor ID,Amount,Type,Status,Created At,Approved At,Balance After,Rejection Reason\n");

        for (WithdrawalDTO withdrawal : withdrawals) {
            csv.append(withdrawal.getId()).append(",");
            csv.append(withdrawal.getInvestorId()).append(",");
            csv.append(withdrawal.getAmount()).append(",");
            csv.append(withdrawal.getType()).append(",");
            csv.append(withdrawal.getStatus()).append(",");
            csv.append(withdrawal.getCreatedAt()).append(",");
            csv.append(withdrawal.getApprovedAt() != null ? withdrawal.getApprovedAt() : "").append(",");
            csv.append(withdrawal.getBalanceAfter()).append(",");
            csv.append(withdrawal.getRejectionReason() != null ? "\"" + withdrawal.getRejectionReason() + "\"" : "").append("\n");
        }

        return csv.toString();
    }
}
