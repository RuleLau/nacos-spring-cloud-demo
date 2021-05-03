package com.rule.common.entity;


import com.rule.common.annotation.Translate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectBoxView {

    private String code;

    private String value;

    @Translate(code = "111", type = SelectBoxView.class)
    private String test;

}
