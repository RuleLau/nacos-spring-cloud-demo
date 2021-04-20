package com.rule.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageInfo {

    /**
     * id
     */
    private Integer id;

    /**
     * 标志
     */
    private String tag;

    /**
     * 请求内容
     */
    private String body;

    /**
     * 类型
     */
    private String type;

    /**
     * 延迟时间 s
     */
    private String delay;

}
