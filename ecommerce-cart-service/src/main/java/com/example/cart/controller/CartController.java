package com.example.cart.controller;


import com.example.cart.domain.dto.AddItemDTO;
import com.example.cart.domain.vo.CartInfoVo;
import com.example.cart.service.ICartService;
import com.example.common.domain.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 购物车信息数据库 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Api("购物车相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final ICartService iCartService;

    @ApiOperation("添加购物车")
    @PostMapping
    public ResponseResult<AddItemDTO> addCart(@RequestBody AddItemDTO addItemDTO){

        return iCartService.addCart(addItemDTO);

    }


    @ApiOperation("清空购物车")
    @DeleteMapping
    public ResponseResult<Void> deleteCartItem(){

        return iCartService.deleteCartItem();

    }


    @ApiOperation("获取购物车信息")
    @GetMapping
    public ResponseResult<CartInfoVo> getCartInfo(){

        return iCartService.getCartInfo();

    }


}
