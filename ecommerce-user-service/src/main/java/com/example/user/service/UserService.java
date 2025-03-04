package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.user.domain.vo.UserInfoVo;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.LogoffDto;
import com.example.user.domain.dto.RegisterDto;
import com.example.user.domain.dto.UserUpdateDto;
import com.example.user.domain.po.User;

public interface UserService extends IService<User> {

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

    /**
     * 封禁或者注销用户
     * @param userId 用户ID
     * @param logoffDto 注销信息
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void disableUser(Long userId, LogoffDto logoffDto) throws UserException, SystemException;

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userUpdateDto 更新信息
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void updateUserInfo(Long userId, UserUpdateDto userUpdateDto) throws UserException, SystemException;

    /**
     * 设置某个用户的权限为管理员或者普通用户
     * @param userId 用户ID
     * @param status true为管理员，false为普通用户
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    void setUserAdminPower(Long userId, boolean status) throws UserException, SystemException;
}
