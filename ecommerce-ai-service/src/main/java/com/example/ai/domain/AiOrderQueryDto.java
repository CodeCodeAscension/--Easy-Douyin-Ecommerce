package com.example.ai.domain;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiResponse(description = "AI订单查询请求DTO")
public class AiOrderQueryDto {
    @Schema(description = "查询内容的自然语言描述", example = "查找最近的订单")
    @NotBlank(message = "查询内容不能为空")
    private String queryContent;

    @Schema(description = "最大结果返回数量", example = "10")
    @Min(value = 1, message = "数字应大于0")
    @NotNull(message = "最大数量不能为空")
    private Integer maxResultCount;

    @Schema(description = "是否需要用户确认下单", example = "false")
    @NotNull(message = "needConfirm不能为空")
    private Boolean needConfirm;
}
