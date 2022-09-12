package com.ninjaone.backendinterviewproject.controller.dto;

import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class CostDTO {
    private BigDecimal cost;

    public CostDTO() {
    }

    public CostDTO(BigDecimal cost) {
        this.cost = cost;
    }

}
