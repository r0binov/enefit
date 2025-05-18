package com.enefit.backend.dto;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class SuccessResponseDto {
    private Integer status;
    private String data;
}
