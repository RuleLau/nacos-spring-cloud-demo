package com.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rule.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
