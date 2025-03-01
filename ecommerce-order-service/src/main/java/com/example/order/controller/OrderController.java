package com.example.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.example.api.domain.dto.order.CancelOrderDto;
import com.example.api.domain.dto.order.PlaceOrderDto;
import com.example.api.domain.dto.order.SearchOrderDto;
import com.example.api.domain.dto.order.UpdateOrderDto;
import com.example.api.domain.po.OrderResult;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.order.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

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
    public ResponseResult<OrderResult> createOrder(@RequestBody PlaceOrderDto placeOrderDto) {

        OrderResult orderResult = iOrderService.createOrder(placeOrderDto);
        log.info("订单创建成功，订单号：{}", orderResult.getOrderId());

        return ResponseResult.success(orderResult);

    }

    @Operation(summary = "修改订单信息")
    @PutMapping
    public ResponseResult<Void> updateOrder(UpdateOrderDto updateOrderDto) {

        Boolean b = iOrderService.updateOrder(updateOrderDto);
        if (!b) {
            return ResponseResult.error(400, "订单状态错误，不能修改");
        }

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
    public ResponseResult<Page<OrderInfoVo>> getAllOrders(@RequestParam Integer pageSize, @RequestParam Integer pageNum) {

        //分页查询订单信息
        PageDTO<OrderInfoVo> page = iOrderService.getAllOrders(pageSize, pageNum);
        log.info("分页查询订单信息成功，订单信息：{}", page);
        return ResponseResult.success(page);

    }

  @Operation(summary = "条件分页查询")
  @GetMapping("/search")
  public ResponseResult<Page<OrderInfoVo>> searchOrders(@RequestParam Integer pageSize,
                                                          @RequestParam Integer PageNum ,
                                                          @RequestBody SearchOrderDto seatchOrderDto) {
        PageDTO<OrderInfoVo> page = iOrderService.searchOrders(pageSize, PageNum, seatchOrderDto);
        log.info("条件分页查询订单信息成功，订单信息：{}", page);


        return ResponseResult.success(page);
    }


}
