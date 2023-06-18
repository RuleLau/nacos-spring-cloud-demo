package com.rule.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDTO implements Serializable {

    private Integer skuNo;

    private Integer quantity;

    private Integer entryId;
}
