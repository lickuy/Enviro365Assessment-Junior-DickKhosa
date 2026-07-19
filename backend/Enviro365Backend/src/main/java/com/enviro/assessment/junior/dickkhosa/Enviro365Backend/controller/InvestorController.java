package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.controller;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.InvestorDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.service.InvestorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investors")
@CrossOrigin(origins = "http://localhost:3000")
public class InvestorController {
    @Autowired
    private InvestorService investorService;

    @GetMapping("/{id}/portfolio")
    public ResponseEntity<InvestorDTO> getInvestorPortfolio(@PathVariable Long id) {
        InvestorDTO investor = investorService.getInvestorWithPortfolio(id);
        return ResponseEntity.ok(investor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorDTO> getInvestor(@PathVariable Long id) {
        InvestorDTO investor = investorService.getInvestor(id);
        return ResponseEntity.ok(investor);
    }
}
