package com.example.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.api.client.UserClient;
import com.example.api.domain.vo.user.UserInfoVo;
import com.example.cart.controller.CartController;
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
import com.example.common.util.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.AttributeSet;
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


    //添加购物车
    @Transactional
    @Override
    public ResponseResult<AddItemDTO> addCart(AddItemDTO addItemDTO) {
        //判断用户是否登录

        Long userId= UserContextUtil.getUserId();
    // userId= 1L;
        if(userId==null){
            return ResponseResult.error(ResultCode.UNAUTHORIZED,new RuntimeException("用户未登录"));
        }
        //判断是否是第一次添加购物车,条件是用户id和购物车未结算的购物车
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).eq(Cart::getStatus, 0).one();
        //如果是第一次添加购物车，创建购物车
        if(cart==null){
            cart=new Cart();
            cart.setUserId(userId);
            cart.setStatus(0);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            this.save(cart);
        }
        //判断购物车中是否已经有该商品,如果有，则更新数量，如果没有，则添加商品
        CartItem cartItem = iCartItemService.lambdaQuery()
                .eq(CartItem::getProductId, addItemDTO.getProductId())
                .eq(CartItem::getCartId, cart.getId())
                .one();
        if(cartItem!=null){
            //更新数量
            cartItem.setQuantity(cartItem.getQuantity()+addItemDTO.getQuantity());
            iCartItemService.updateById(cartItem);
        }else{
            //添加商品
            CartItem cartItem1 = new CartItem();
            BeanUtils.copyProperties(addItemDTO,cartItem1);
            cartItem1.setCartId(cart.getId());
            cartItem1.setCreateTime(LocalDateTime.now());
            cartItem1.setUpdateTime(LocalDateTime.now());
            iCartItemService.save(cartItem1);
        }
        return ResponseResult.success();
    }


    //清空购物车商品
    @Transactional
    @Override
    public ResponseResult<Void> deleteCartItem() {
       //获取用户id
        Long userId= UserContextUtil.getUserId();
       // userId= 1L;
        if(userId==null){
            return ResponseResult.error(ResultCode.UNAUTHORIZED,new RuntimeException("用户未登录"));
        }
        //查询购物车表获取购物车id
        Cart cart = this.lambdaQuery().eq(Cart::getUserId, userId).eq(Cart::getStatus, 0).one();
        if(cart==null){
            return ResponseResult.error(ResultCode.FAILED_DEPENDENCY,new RuntimeException("购物车不存在"));
        }
        boolean remove = iCartItemService.lambdaUpdate()
                .eq(CartItem::getCartId, cart.getId())
                .remove();
        if(!remove){
            return ResponseResult.error(ResultCode.FAILED_DEPENDENCY,new RuntimeException("清空购物车失败"));
        }
        return ResponseResult.success();
    }

    //获取购物车信息
    @Override
    public ResponseResult<CartInfoVo> getCartInfo() {
        Long userId= UserContextUtil.getUserId();
       // userId= 1L;
        if(userId==null){
            return ResponseResult.error(ResultCode.UNAUTHORIZED,new RuntimeException("用户未登录"));
        }
        //获取购物车信息
        Cart cart = this.lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getStatus, 0)
                .one();
        CartInfoVo cartInfoVo = new CartInfoVo();

        if(cart!=null){
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

        }
        return ResponseResult.success(cartInfoVo);

    }


}
