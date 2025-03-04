package com.example.cart.service.impl;

import com.example.api.client.ProductClient;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.api.enums.ProductStatusEnum;
import com.example.cart.domain.dto.AddItemDTO;
import com.example.cart.domain.po.Cart;
import com.example.cart.domain.po.CartItem;
import com.example.cart.domain.vo.CartInfoVo;
import com.example.cart.domain.vo.CartItemInfo;
import com.example.cart.mapper.CartMapper;
import com.example.cart.service.ICartItemService;
import com.example.cart.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.NotFoundException;
import com.example.common.exception.UserException;
import com.example.common.util.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 购物车信息数据库 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final ICartItemService iCartItemService;
    private final ProductClient productClient;

    //添加购物车
    @Transactional
    @Override
    public void addCart(AddItemDTO addItemDTO) {

        //判断用户是否登录
        Long userId = UserContextUtil.getUserId();

        //判断是否是第一次添加购物车,条件是用户id和购物车未结算的购物车
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).one();
        //如果是第一次添加购物车，创建购物车
        if(cart == null){
            cart = new Cart();
            cart.setUserId(userId);
            cart.setStatus(0);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            this.save(cart);
        }

        //判断商品是否有货,以及商品是否下架
        ResponseResult<ProductInfoVo> productInfoById = productClient.getProductInfoById(addItemDTO.getProductId());
        if(productInfoById.getCode() != ResultCode.SUCCESS || productInfoById.getData() == null) {
            throw new UserException(productInfoById.getCode(), productInfoById.getMsg());
        }
        if(productInfoById.getData().getStatus() != ProductStatusEnum.PUT_ON) {
            throw new BadRequestException("商品未上架");
        }
        if(productInfoById.getData().getStock() < addItemDTO.getQuantity()) {
            throw new BadRequestException("商品存货不足");
        }

        //判断购物车中是否已经有该商品,如果有，则更新数量，如果没有，则添加商品
        CartItem cartItem = iCartItemService.lambdaQuery()
                .eq(CartItem::getProductId, addItemDTO.getProductId())
                .eq(CartItem::getCartId, cart.getId())
                .one();
        if(cartItem != null){
            //更新数量
            cartItem.setQuantity(cartItem.getQuantity()+addItemDTO.getQuantity());
            iCartItemService.updateById(cartItem);
        } else {
            //添加商品
            CartItem cartItem1 = new CartItem();
            BeanUtils.copyProperties(addItemDTO,cartItem1);
            cartItem1.setCartId(cart.getId());
            cartItem1.setCreateTime(LocalDateTime.now());
            cartItem1.setUpdateTime(LocalDateTime.now());
            iCartItemService.save(cartItem1);
        }
    }

    //清空购物车商品
    @Transactional
    @Override
    public Boolean deleteCartItem() {
        //获取用户id
        Long userId= UserContextUtil.getUserId();

        //查询购物车表获取购物车id
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).one();
        if(cart == null) {
            return false;
        }
        return iCartItemService.lambdaUpdate()
                .eq(CartItem::getCartId, cart.getId())
                .remove();
    }

    //获取购物车信息
    @Override
    public CartInfoVo getCartInfo() {
        Long userId= UserContextUtil.getUserId();
        //获取购物车信息
        Cart cart = this.lambdaQuery()
                .eq(Cart::getUserId, userId)
                .one();
        CartInfoVo cartInfoVo = new CartInfoVo();

        if(cart != null){
            cartInfoVo.setId(cart.getId());
            cartInfoVo.setUserId(cart.getUserId());
            cartInfoVo.setStatus(cart.getStatus());
            cartInfoVo.setCreateTime(cart.getCreateTime());
            cartInfoVo.setUpdateTime(cart.getUpdateTime());
            List<CartItem> list = iCartItemService.lambdaQuery()
                    .eq(CartItem::getCartId, cart.getId())
                    .list();
            //封装商品信息到c
            List<CartItemInfo> cartItemInfoList = list.stream()
                    .map(item -> {
                        CartItemInfo info = new CartItemInfo();
                        BeanUtils.copyProperties(item, info); // 拷贝同名属性
                        return info;
                    })
                    .collect(Collectors.toList());
            cartInfoVo.setCartItems(cartItemInfoList);
            return  cartInfoVo;
        } else {
            throw new NotFoundException("该用户还没有购物车信息");
        }
    }
}
