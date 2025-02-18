package com.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.cache.MybatisRedisCache;
import com.example.user.domain.po.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface UserMapper extends BaseMapper<User> {
}
