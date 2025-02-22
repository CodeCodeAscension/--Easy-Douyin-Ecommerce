package com.example.api.client.fallback;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.api.client.ProductClient;
import com.example.api.domain.dto.product.AddProductDto;
import com.example.api.domain.dto.product.DecProductDto;
import com.example.api.domain.dto.product.ListProductsDto;
import com.example.api.domain.dto.product.SearchProductsDto;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.common.domain.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductClientFallBack implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient() {
            @Override
            public ResponseResult<ProductInfoVo> getProductInfoById(Long productId) {
                log.error("product-service-exception:getProductInfoById, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<IPage<ProductInfoVo>> getProductInfoByCategory(ListProductsDto listProductsDto) {
                log.error("product-service-exception:getProductInfoByCategory, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<IPage<ProductInfoVo>> seachProductInfo(SearchProductsDto searchProductsDto) {
                log.error("product-service-exception:seachProductInfo, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> addProductStock(AddProductDto addProductDto) {
                log.error("product-service-exception:addProductStock, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }

            @Override
            public ResponseResult<Object> decProductStock(DecProductDto decProductDto) {
                log.error("product-service-exception:decProductStock, "+cause.getMessage());
                return ResponseResult.errorFeign(cause);
            }
        };
    }
}
