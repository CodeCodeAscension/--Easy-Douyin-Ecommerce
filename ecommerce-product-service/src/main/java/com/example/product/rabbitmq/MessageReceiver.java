package com.example.product.rabbitmq;

import com.example.common.domain.message.PayFailMessage;
import com.example.common.domain.message.PaySuccessMessage;
import com.example.product.domain.dto.AddProductDto;
import com.example.product.domain.dto.AddProductSoldDto;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageReceiver {

    @Resource
    private IProductService productService;

    @RabbitListener(queues = "pay.success.product")
    public void handleMessage(PaySuccessMessage message) {
        log.info("收到支付成功的消息，订单号：{}", message.getOrderId());
        // 增加商品的销量
        message.getProducts().forEach(product ->
            productService.addProductSales(new AddProductSoldDto(product.getProductId(), product.getQuantity()))
        );
    }

    @RabbitListener(queues = "pay.fail.product")
    public void handleMessage(PayFailMessage message) {
        log.info("收到支付失败的消息，订单号：{}", message.getOrderId());
        if(message.getAddProductIds() == null || message.getAddProductIds().isEmpty()) {
            return;
        }
        // 恢复库存
        message.getProducts().forEach(productQuantity -> {
            if(message.getAddProductIds().contains(productQuantity.getProductId())) {
                productService.addProductStock(new AddProductDto(productQuantity.getProductId(), productQuantity.getQuantity()));
            }
        });
    }
}
