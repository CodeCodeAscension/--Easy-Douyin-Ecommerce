package com.example.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.api.domain.vo.order.AddressInfoVo;
import com.example.common.domain.ResponseResult;
import com.example.common.util.UserContextUtil;
import com.example.order.domain.dto.AddressDto;
import com.example.order.domain.dto.AddressUpdateDto;
import com.example.order.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户收货地址信息 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-02-28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
@Tag(name = "收货地址相关接口", description = "收货地址相关接口")
public class AddressController {

    private final IAddressService iAddressService;

    @PostMapping
    @Operation(summary = "添加地址信息")
    public ResponseResult<Void> addAddress(@RequestBody @Validated AddressDto addressDto) {
        Long userId = UserContextUtil.getUserId();
        iAddressService.addAddress(userId, addressDto);
        return ResponseResult.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取地址信息")
    public ResponseResult<AddressInfoVo> getAddress(@PathVariable("id") Long id) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(iAddressService.getAddressInfo(userId, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除地址信息")
    public ResponseResult<Void> deleteAddress(@PathVariable("id") Long id) {
        Long userId = UserContextUtil.getUserId();
        iAddressService.deleteAddress(userId, id);
        return ResponseResult.success();
    }

    @PutMapping
    @Operation(summary = "修改地址信息")
    public ResponseResult<Void> updataAddress(@RequestBody @Validated AddressUpdateDto addressUpdateDto) {
        Long userId = UserContextUtil.getUserId();
        iAddressService.updateAddress(userId, addressUpdateDto);
        return ResponseResult.success();
    }

    @GetMapping
    @Operation(summary = "获取某个用户所有地址信息")
    public ResponseResult<IPage<AddressInfoVo>> getAllAddresses(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        Long userId = UserContextUtil.getUserId();
        return ResponseResult.success(iAddressService.getAddressesByUserId(userId, pageNum, pageSize));
    }

}
