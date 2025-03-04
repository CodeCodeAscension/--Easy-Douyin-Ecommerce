package com.example.order.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AddressDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "街道地址")
    @NotEmpty
    private String streetAddress;

    @Schema(description = "城市")
    @NotEmpty
    private String city;

    @Schema(description = "省份")
    @NotEmpty
    private String province;

    @Schema(description = "国家")
    @NotEmpty
    private String country;

    @Schema(description = "邮政编码")
    private String zipCode;
}
