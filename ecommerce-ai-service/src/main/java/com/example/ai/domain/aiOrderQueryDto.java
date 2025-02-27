package com.example.ai.domain;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiResponse(description = "AI订单查询请求DTO")
public class aiOrderQueryDto {

    @Schema(description = "查询内容的自然语言描述", example = "查找最近的订单")
    private String queryContent;

    @Schema(description = "最大结果返回数量", example = "10")
    private Integer maxResultCount;

    @Schema(description = "是否需要用户确认下单", example = "true")
    private Boolean needConfirm;


}
