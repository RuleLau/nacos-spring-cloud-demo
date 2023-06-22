package com.rule.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionStatementDTO implements Serializable {

    private Integer orderNo;

    private String payStatus;
    private Integer entryId;
}
