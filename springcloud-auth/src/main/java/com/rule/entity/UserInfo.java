package com.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "t_user")
public class UserInfo implements Serializable {

    @TableId(value = "ID")
    private Integer id;

    private String username;

    private String password;


}
