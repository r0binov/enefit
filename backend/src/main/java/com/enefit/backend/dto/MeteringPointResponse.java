package com.enefit.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MeteringPointResponse {
    private Long id;
    private String address;
}
