package com.example.user.controller.v1;

import com.example.auth.util.TokenRedisUtil;
import com.example.common.domain.ResponseResult;
import com.example.user.domain.dto.BannedDto;
import com.example.user.domain.dto.LogoffDto;
import com.example.user.domain.dto.UpdatePowerDto;
import com.example.user.enums.UserStatusEnum;
import com.example.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *     管理员操作用户信息前端控制器
 * </p>
 * @author vlsmb
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/admin")
@Slf4j
@Tag(name = "管理员操作接口", description = "管理员操作接口")
public class AdminController {
    private final UserService userService;
    private final TokenRedisUtil redisUtil;

    @DeleteMapping
    @Operation(summary = "封禁某个用户")
    public ResponseResult<Object> deleteUser(@RequestBody @Validated BannedDto dto) {
        LogoffDto logoffDto = new LogoffDto();
        logoffDto.setStatus(UserStatusEnum.BANNED);
        logoffDto.setReason(dto.getReason());
        userService.disableUser(dto.getUserId(), logoffDto);
        // 使token失效
        redisUtil.removeToken(dto.getUserId());
        return ResponseResult.success();
    }

    @PutMapping
    @Operation(summary = "修改某个用户权限")
    public ResponseResult<Object> updateUserPower(@RequestBody @Validated UpdatePowerDto dto) {
        userService.setUserAdminPower(dto.getUserId(), dto.getStatus());
        // 使token失效
        redisUtil.removeToken(dto.getUserId());
        return ResponseResult.success();
    }
}
