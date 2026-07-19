package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.service;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.InvestorDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Investor;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.exception.ResourceNotFoundException;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.mapper.InvestorMapper;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.InvestorRepository;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvestorServiceTest {

    @Mock
    private InvestorRepository investorRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private InvestorMapper investorMapper;

    @InjectMocks
    private InvestorService investorService;

    private Investor testInvestor;
    private InvestorDTO testInvestorDTO;

    @BeforeEach
    public void setUp() {
        testInvestor = new Investor();
        testInvestor.setId(1L);
        testInvestor.setFirstName("John");
        testInvestor.setLastName("Doe");
        testInvestor.setEmail("john@example.com");
        // Use a non-retirement age for the default test investor to avoid time-sensitive failures
        testInvestor.setDateOfBirth(LocalDate.of(1990, 5, 15));
        testInvestor.setPhoneNumber("0123456789");
        testInvestor.setTotalBalance(50000.0);

        testInvestorDTO = new InvestorDTO();
        testInvestorDTO.setId(1L);
        testInvestorDTO.setFirstName("John");
        testInvestorDTO.setLastName("Doe");
    }

    @Test
    public void testGetInvestor_Success() {
        when(investorRepository.findById(1L)).thenReturn(Optional.of(testInvestor));
        when(investorMapper.toDTO(testInvestor)).thenReturn(testInvestorDTO);

        InvestorDTO result = investorService.getInvestor(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(investorRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetInvestor_NotFound() {
        when(investorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            investorService.getInvestor(999L);
        });
    }

    @Test
    public void testGetInvestorAge() {
        int expected = java.time.Period.between(testInvestor.getDateOfBirth(), java.time.LocalDate.now()).getYears();
        assertEquals(expected, testInvestor.getAge());
    }

    @Test
    public void testIsRetirementEligible_False() {
        assertFalse(testInvestor.isRetirementEligible());
    }

    @Test
    public void testIsRetirementEligible_True() {
        Investor oldInvestor = new Investor();
        oldInvestor.setDateOfBirth(LocalDate.of(1950, 1, 1));
        assertTrue(oldInvestor.isRetirementEligible());
    }
}
