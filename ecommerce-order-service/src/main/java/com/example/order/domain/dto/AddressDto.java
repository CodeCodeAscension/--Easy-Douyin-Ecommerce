package com.example.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AddressDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "街道地址")
    @NotBlank
    private String streetAddress;

    @Schema(description = "城市")
    @NotBlank
    private String city;

    @Schema(description = "省份")
    @NotBlank
    private String province;

    @Schema(description = "国家")
    @NotBlank
    private String country;

    @Schema(description = "邮政编码")
    @NotBlank
    private String zipCode;
}
