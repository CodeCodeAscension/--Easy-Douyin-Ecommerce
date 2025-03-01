package com.example.product.rabbitmq;

import com.example.api.client.OrderClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.message.PayFailMessage;
import com.example.common.domain.message.PaySuccessMessage;
import com.example.product.domain.dto.AddProductDto;
import com.example.product.domain.dto.AddProductSoldDto;
import com.example.product.domain.dto.DecProductDto;
import com.example.product.service.IProductService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.RabbitMessageFuture;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageReceiver {

    @Resource
    private OrderClient orderClient;

    @Resource
    private IProductService productService;

    @RabbitListener(queues = "pay.success.product")
    public void handleMessage(PaySuccessMessage message) {
        System.out.println("收到消息: " + message);
        // 在此执行业务逻辑（如调用其他方法、处理数据等）
        String orderId = message.getOrderId();
        ResponseResult<OrderInfoVo> order = orderClient.getOrderById(orderId);
        List<CartItem> cartItemList = order.getData().getCartItems();
        // 获取商品id和数量
        for (CartItem cartItem : cartItemList) {
            AddProductSoldDto addProductSoldDto = new AddProductSoldDto(cartItem.getProductId(), cartItem.getQuantity());
            // 调用商品服务加库存
            productService.addProductSales(addProductSoldDto);
        }
    }

    @RabbitListener(queues = "pay.fail.product")
    public void handleMessage(PayFailMessage message) {
        System.out.println("收到消息: " + message);
        // 在此执行业务逻辑（如调用其他方法、处理数据等）
        String orderId = message.getOrderId();
        ResponseResult<OrderInfoVo> order = orderClient.getOrderById(orderId);
        List<CartItem> cartItemList = order.getData().getCartItems();
        // 获取商品id和数量
        for (CartItem cartItem : cartItemList) {
            AddProductDto addProductDto = new AddProductDto(cartItem.getProductId(), cartItem.getQuantity());
            // 调用商品服务加库存
            productService.addProductStock(addProductDto);
        }
    }
}
