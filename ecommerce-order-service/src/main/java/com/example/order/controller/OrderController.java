package com.example.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.vo.order.PlaceOrderVo;
import com.example.order.domain.dto.UpdateOrderDto;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.exception.BadRequestException;
import com.example.common.util.UserContextUtil;
import com.example.order.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单信息数据库 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name = "订单相关接口")
public class OrderController {

    private final IOrderService iOrderService;

    @Operation(summary = "创建订单")
    @PostMapping
    public ResponseResult<PlaceOrderVo> createOrder(@RequestBody @Validated PlaceOrderDto placeOrderDto) {
        Long userId = UserContextUtil.getUserId();
        List<CartItem> item = placeOrderDto.getCartItems();
        if (item != null && !item.isEmpty()) {
            item.forEach(cartItem -> {
                if(cartItem.getProductId() == null || cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                    throw new BadRequestException("购物车参数错误");
                }
            });
        }
        OrderResult orderResult = iOrderService.createOrder(userId, placeOrderDto);
        log.info("订单创建成功，订单号：{}", orderResult.getOrderId());
        PlaceOrderVo placeOrderVo = new PlaceOrderVo();
        placeOrderVo.setOrder(orderResult);
        return ResponseResult.success(placeOrderVo);
    }

    @Operation(summary = "修改订单信息")
    @PutMapping
    public ResponseResult<Void> updateOrder(@RequestBody @Validated UpdateOrderDto updateOrderDto) {
        List<CartItem> item = updateOrderDto.getCartItems();
        if (item != null && !item.isEmpty()) {
            item.forEach(cartItem -> {
                if(cartItem.getProductId() == null || cartItem.getQuantity() == null || cartItem.getQuantity() <= 0) {
                    throw new BadRequestException("购物车参数错误");
                }
            });
        }
        Long userId = UserContextUtil.getUserId();
        iOrderService.updateOrder(userId, updateOrderDto);
        return ResponseResult.success();
    }

    @Operation(summary = "订单id查询订单信息")
    @GetMapping("/{orderId}")
    public ResponseResult<OrderInfoVo> getOrderById(@PathVariable("orderId") String orderId) {
        OrderInfoVo orderInfoVo = iOrderService.getOrderById(orderId);
        return ResponseResult.success(orderInfoVo);
    }

    @Operation(summary = "分页查询订单信息")
    @GetMapping
    public ResponseResult<Page<OrderInfoVo>> getAllOrders(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        //分页查询订单信息
        Long userId = UserContextUtil.getUserId();
        PageDTO<OrderInfoVo> page = iOrderService.getAllOrders(userId, pageSize, pageNum);
        log.info("分页查询订单信息成功，订单信息：{}", page);
        return ResponseResult.success(page);
    }

    @Operation(summary = "条件分页查询")
    @GetMapping("/search")
    public ResponseResult<Page<OrderInfoVo>> searchOrders(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestBody SearchOrderDto seatchOrderDto) {
        Long userId = UserContextUtil.getUserId();
        PageDTO<OrderInfoVo> page = iOrderService.searchOrders(userId, pageSize, pageNum, seatchOrderDto);
        log.info("条件分页查询订单信息成功，订单信息：{}", page);
        return ResponseResult.success(page);
    }

    @Operation(summary = "取消订单")
    @DeleteMapping("/{orderId}")
    public ResponseResult<Void> deleteOrder(@PathVariable("orderId") String orderId) {
        Long userId = UserContextUtil.getUserId();
        iOrderService.cancelOrder(userId, orderId);
        return ResponseResult.success();
    }
}
