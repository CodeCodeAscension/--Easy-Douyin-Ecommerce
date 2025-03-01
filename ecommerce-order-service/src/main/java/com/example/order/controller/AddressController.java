package com.example.order.controller;


import com.example.api.domain.po.Address;
import com.example.common.domain.ResponseResult;
import com.example.order.service.IAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/orders/addresses")
@Tag(name="收货地址相关接口")
public class AddressController {

    private final IAddressService iAddressService;


    @PostMapping
    @Operation(summary = "添加地址信息")
    public ResponseResult<Void> addAddress(@RequestBody Address address) {

        boolean b = iAddressService.save(address);
        if (b) {
            return ResponseResult.success();
        }else {
            return ResponseResult.error(500,"新增失败");
        }

    }
    @GetMapping("/{id}")
    @Operation(summary = "获取地址信息")
    public ResponseResult<Address> getAddress(@PathVariable("id") Long id) {

        Address address = iAddressService.getById(id);
        if (address != null) {
            return ResponseResult.success(address);
        }else {
            return ResponseResult.error(500,"获取失败");
        }

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除地址信息")
    public ResponseResult<Void> deleteAddress(@PathVariable("id") Long id) {

        boolean b = iAddressService.removeById(id);
        if (b) {
            return ResponseResult.success();
        }else {
            return ResponseResult.error(500,"删除失败");
        }

    }

    @PutMapping
    @Operation(summary = "修改地址信息")
    public ResponseResult<Void> updataAddress(@RequestBody Address address) {

        boolean b = iAddressService.saveOrUpdate(address);
        if (b) {
            return ResponseResult.success();
        }else {
            return ResponseResult.error(500,"添加失败");
        }

    }


}
