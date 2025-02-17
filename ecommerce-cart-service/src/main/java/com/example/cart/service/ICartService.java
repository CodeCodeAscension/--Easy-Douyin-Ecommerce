package com.example.cart.service;

import com.example.cart.domain.dto.AddItemDTO;
import com.example.cart.domain.po.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cart.domain.vo.CartInfoVo;
import com.example.common.domain.ResponseResult;

/**
 * <p>
 * 购物车信息数据库 服务类
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
public interface ICartService extends IService<Cart> {

    //添加购物车
    ResponseResult<AddItemDTO> addCart(AddItemDTO addItemDTO);

    //清空购物车商品
    ResponseResult<Void> deleteCartItem();

    //获取购物车信息
    ResponseResult<CartInfoVo> getCartInfo();
}
