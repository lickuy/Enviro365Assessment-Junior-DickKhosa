package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.service;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.InvestorDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.PortfolioDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.ProductDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Investor;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Portfolio;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Product;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.mapper.InvestorMapper;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.InvestorRepository;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestorService {
    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private InvestorMapper investorMapper;

    public InvestorDTO getInvestorWithPortfolio(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));

        InvestorDTO dto = investorMapper.toDTO(investor);

        List<Portfolio> portfolios = portfolioRepository.findByInvestorId(investorId);
        List<PortfolioDTO> portfolioDTOs = portfolios.stream()
                .map(this::convertPortfolioToDTO)
                .collect(Collectors.toList());

        dto.setPortfolios(portfolioDTOs);
        return dto;
    }

    public InvestorDTO getInvestor(Long investorId) {
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new ResourceNotFoundException("Investor not found with id: " + investorId));
        return investorMapper.toDTO(investor);
    }

    private PortfolioDTO convertPortfolioToDTO(Portfolio portfolio) {
        PortfolioDTO dto = new PortfolioDTO();
        dto.setId(portfolio.getId());
        dto.setName(portfolio.getName());
        dto.setBalance(portfolio.getBalance());

        List<ProductDTO> productDTOs = portfolio.getProducts().stream()
                .map(this::convertProductToDTO)
                .collect(Collectors.toList());
        dto.setProducts(productDTOs);

        return dto;
    }

    private ProductDTO convertProductToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setType(product.getType());
        dto.setValue(product.getValue());
        dto.setUnitPrice(product.getUnitPrice());
        return dto;
    }
}
