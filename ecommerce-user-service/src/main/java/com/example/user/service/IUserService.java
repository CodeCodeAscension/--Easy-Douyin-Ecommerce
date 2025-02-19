package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.api.domain.vo.user.UserInfoVo;
import com.example.common.exception.DatabaseException;
import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.RegisterDto;
import com.example.user.domain.po.User;

public interface IUserService extends IService<User> {

    /**
     * 根据用户邮箱去找是否存在可用的账号，并返回用户ID
     * 如果不存在则返回null
     * @param email 用户邮箱
     * @return 用户可用的ID
     * @author vlsmb
     */
    Long findEnabledUserId(String email);

    /**
     * 注册新用户
     * @param registerDto dto
     * @throws DatabaseException 数据库操作异常
     * @return 新的用户ID
     */
    Long register(RegisterDto registerDto) throws DatabaseException;

    /**
     * 检查登陆信息
     * @param loginDto dto
     * @return 用户存在且密码正确时返回用户对象，否则返回null
     */
    User checkPassword(LoginDto loginDto);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVo getUserInfo(Long userId);
}
