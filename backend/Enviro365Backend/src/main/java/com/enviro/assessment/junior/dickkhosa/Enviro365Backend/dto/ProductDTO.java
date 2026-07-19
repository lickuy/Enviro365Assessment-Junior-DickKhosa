package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String type;
    private Double value;
    private Double unitPrice;
}
