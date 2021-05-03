package com.rule.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rule.common.annotation.Translate;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "t_user")
public class UserInfo {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限
     */
    private String authorities;

    @Translate(code = "222", type = SelectBoxView.class)
    private List<SelectBoxView> codeList;

    private int num;

    private SelectBoxView selectBoxView = new SelectBoxView();
}
