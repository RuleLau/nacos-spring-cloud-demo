package com.rule.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rule.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<MessageInfo> {
}
