package com.rule.mapper;


import com.rule.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {


    List<User> findByNameList(@Param("nameList") List<String> nameList);
}