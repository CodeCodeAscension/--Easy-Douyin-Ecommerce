package com.example.cart.controller.v1;


import com.example.api.domain.po.CartItem;
import com.example.cart.domain.dto.AddItemDTO;
import com.example.cart.domain.vo.CartInfoVo;
import com.example.cart.service.ICartItemService;
import com.example.cart.service.ICartService;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 购物车信息数据库 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Tag(name = "购物车接口", description = "购物车接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final ICartService iCartService;
    private final ICartItemService iCartItemService;

    @Operation(summary = "添加购物车")
    @PostMapping
    public ResponseResult<AddItemDTO> addCart(@RequestBody @Validated AddItemDTO addItemDTO){
        iCartService.addCart(addItemDTO);
        return ResponseResult.success();
   }

    @Operation(summary = "清空购物车")
    @DeleteMapping
    public ResponseResult<Void> deleteCartItem(){
        if(!iCartService.deleteCartItem()) {
            throw new NotFoundException("该用户的购物车内暂无商品");
        }
        return ResponseResult.success();
    }

    @Operation(summary = "获取购物车信息")
    @GetMapping
    public ResponseResult<CartInfoVo> getCartInfo(){

        CartInfoVo cartInfo = iCartService.getCartInfo();

        if (cartInfo!=null){
            return ResponseResult.success(cartInfo);
        }
        return ResponseResult.error(ResultCode.SERVER_ERROR,"获取购物车信息失败");
    }

    @Operation(summary = "获得某个购物车商品ID和数量")
    @GetMapping("/items/{id}")
    public ResponseResult<CartItem> getCartItem(@PathVariable Long id) {
        com.example.cart.domain.po.CartItem item = iCartItemService.getById(id);
        if(item == null) {
            throw new NotFoundException("不存在该cart-item");
        }
        return ResponseResult.success(CartItem.builder()
                        .cartItemId(item.getId())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build());
    }

}
