package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.domain.vo.UserInfoVo;
import com.example.auth.enums.UserPower;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.common.exception.SystemException;
import com.example.common.exception.UserException;
import com.example.user.domain.dto.LoginDto;
import com.example.user.domain.dto.LogoffDto;
import com.example.user.domain.dto.RegisterDto;
import com.example.user.domain.dto.UserUpdateDto;
import com.example.user.domain.po.User;
import com.example.user.enums.UserStatusEnum;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserService;
import com.example.auth.util.BCryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *     用户数据库服务类
 * </p>
 * @author vlsmb
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final BCryptUtil bCryptUtil;

    /**
     * 寻找是否存在可用的用户对象
     * @param email 用户邮箱
     * @return 用户对象
     */
    private User findEnabledUser(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        queryWrapper.eq(User::getStatus, UserStatusEnum.NORMAL);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Long findEnabledUserId(String email) {
        User user = findEnabledUser(email);
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

    @Override
    public Long register(RegisterDto registerDto) throws DatabaseException {
        String email = registerDto.getEmail();
        String password = bCryptUtil.hashPassword(registerDto.getPassword().trim());
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(UserStatusEnum.NORMAL);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        if(!this.save(user)) {
            throw new DatabaseException("用户信息保存失败");
        }
        return user.getUserId();
    }

    @Override
    public User checkPassword(LoginDto loginDto) {
        User user = findEnabledUser(loginDto.getEmail());
        if(user == null) {
            // 用户不存在
            return null;
        }
        if(bCryptUtil.matches(loginDto.getPassword().trim(), user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public UserInfoVo getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if(user == null) {
            return null;
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }

    @Override
    public void disableUser(Long userId, LogoffDto logoffDto) throws UserException, SystemException {
        // 先判断是否存在
        if(!this.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId, userId)
                .eq(User::getStatus, UserStatusEnum.NORMAL))) {
            throw new NotFoundException("要封禁或注销的用户ID不存在");
        }
        User user = new User();
        user.setUserId(userId);
        user.setStatus(logoffDto.getStatus());
        user.setDisableReason(logoffDto.getReason());
        user.setUpdateTime(LocalDateTime.now());
        if(!this.updateById(user)) {
            throw new DatabaseException("MybatisPlus更新数据库失败");
        }
    }

    @Override
    public void updateUserInfo(Long userId, UserUpdateDto userUpdateDto) throws UserException, SystemException {
        // 先判断是否存在
        if(!this.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId, userId)
                .eq(User::getStatus, UserStatusEnum.NORMAL))) {
            throw new NotFoundException("用户ID不存在或不可用");
        }
        User user = new User();
        user.setUserId(userId);
        user.setUpdateTime(LocalDateTime.now());
        BeanUtils.copyProperties(userUpdateDto, user);
        if(!this.updateById(user)) {
            throw new DatabaseException("MybatisPlus更新数据库失败");
        }
    }

    @Override
    public void setUserAdminPower(Long userId, boolean status) throws UserException, SystemException {
        // 先判断是否存在
        if(!this.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getUserId, userId)
                .eq(User::getStatus, UserStatusEnum.NORMAL))) {
            throw new NotFoundException("用户ID不存在或不可用");
        }
        User user = this.getById(userId);
        user.setPower(status ? UserPower.ADMIN : UserPower.USER);
        if(!this.updateById(user)) {
            throw new DatabaseException("MybatisPlus更新数据库失败");
        }
    }
}
