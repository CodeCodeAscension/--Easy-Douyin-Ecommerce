package com.example.product.controller;

import com.example.api.domain.dto.product.DecProductDto;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.product.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inner/products")
@Tag(name="内部方法", description = "内部方法")
public class ProductInnerController {
    private final IProductService iProductService;

    @PutMapping("/dec")
    @Operation(summary = "减少库存")
    public ResponseResult<Object> decProductStock(@RequestBody @Validated DecProductDto decProductDto) {
        UserContextUtil.getUserId();
        iProductService.decProductStock(decProductDto);
        return ResponseResult.success();
    }
}
