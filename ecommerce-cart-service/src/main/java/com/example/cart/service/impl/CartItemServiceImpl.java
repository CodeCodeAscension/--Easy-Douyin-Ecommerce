package com.example.cart.service.impl;

import com.example.cart.domain.po.CartItem;
import com.example.cart.mapper.CartItemMapper;
import com.example.cart.service.ICartItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车物品数据库 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-14
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements ICartItemService {

}
