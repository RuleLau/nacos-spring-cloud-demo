package com.rule.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rule.common.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserInfo> {
}
