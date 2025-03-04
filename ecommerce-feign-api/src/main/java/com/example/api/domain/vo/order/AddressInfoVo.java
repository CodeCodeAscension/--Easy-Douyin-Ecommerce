package com.example.api.domain.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户收货地址信息
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户收货地址信息")
public class AddressInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "收货地址ID")
    private Long id;

    @Schema(description = "街道地址")
    private String streetAddress;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "邮政编码")
    private String zipCode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
