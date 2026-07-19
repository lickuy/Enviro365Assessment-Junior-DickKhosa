package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.mapper;

import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto.InvestorDTO;
import com.enviro.assessment.junior.dickkhosa.Enviro365Backend.entity.Investor;
import org.springframework.stereotype.Component;

@Component
public class InvestorMapper {
    public InvestorDTO toDTO(Investor investor) {
        if (investor == null) {
            return null;
        }

        InvestorDTO dto = new InvestorDTO();
        dto.setId(investor.getId());
        dto.setFirstName(investor.getFirstName());
        dto.setLastName(investor.getLastName());
        dto.setEmail(investor.getEmail());
        dto.setDateOfBirth(investor.getDateOfBirth());
        dto.setPhoneNumber(investor.getPhoneNumber());
        dto.setTotalBalance(investor.getTotalBalance());
        dto.setAge(investor.getAge());
        dto.setIsRetirementEligible(investor.isRetirementEligible());
        return dto;
    }

    public Investor toEntity(InvestorDTO dto) {
        if (dto == null) {
            return null;
        }

        Investor investor = new Investor();
        investor.setId(dto.getId());
        investor.setFirstName(dto.getFirstName());
        investor.setLastName(dto.getLastName());
        investor.setEmail(dto.getEmail());
        investor.setDateOfBirth(dto.getDateOfBirth());
        investor.setPhoneNumber(dto.getPhoneNumber());
        investor.setTotalBalance(dto.getTotalBalance());
        return investor;
    }
}
