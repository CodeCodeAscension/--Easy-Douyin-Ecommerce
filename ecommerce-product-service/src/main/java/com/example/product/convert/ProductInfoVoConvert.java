package com.example.product.convert;

import com.example.product.domain.po.Product;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.product.service.ICategoryService;
import com.example.product.service.IProCateRelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductInfoVoConvert {

    private final IProCateRelService iProCateRelService;
    private final ICategoryService iCategoryService;

    /**
     * 将 Product 对象转换为 ProductInfoVo 对象
     *
     * @param product 商品实体
     * @return ProductInfoVo
     */
    public ProductInfoVo convertToProductInfoVo(Product product) {
        // 封装商品信息
        return ProductInfoVo.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .sold(product.getSold())
                .stock(product.getStock())
                .merchantName(product.getMerchantName())
                .categories(iProCateRelService.getProductCategoryNames(product.getId()))
                .status(product.getStatus())
                .createTime(product.getCreateTime())
                .updateTime(product.getUpdateTime())
                .build();
    }
}
