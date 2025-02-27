package com.example.cart.controller;


import com.example.cart.domain.dto.AddItemDTO;
import com.example.cart.domain.vo.CartInfoVo;
import com.example.cart.service.ICartService;
import com.example.common.domain.ResponseResult;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.responses.ApiResponse;
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
//@Api("购物车相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final ICartService iCartService;

//    @ApiOperation("添加购物车")
    @PostMapping
    public ResponseResult<AddItemDTO> addCart(@RequestBody AddItemDTO addItemDTO){

        AddItemDTO addItemDTO1 = iCartService.addCart(addItemDTO);
        if (addItemDTO1!=null){
            return ResponseResult.success(addItemDTO1);
        }

        return ResponseResult.error(500,"添加购物车失败");
    }


   // @ApiOperation("清空购物车")
    @DeleteMapping
    public ResponseResult<Void> deleteCartItem(){

        Boolean b = iCartService.deleteCartItem();

        if (b){
            return ResponseResult.success();
        }
        return ResponseResult.error(500,"清空购物车失败");

    }


   // @ApiOperation("获取购物车信息")

    @GetMapping
    public ResponseResult<CartInfoVo> getCartInfo(){

        CartInfoVo cartInfo = iCartService.getCartInfo();

        if (cartInfo!=null){
            return ResponseResult.success(cartInfo);
        }
        return ResponseResult.error(500,"获取购物车信息失败");

    }


}
