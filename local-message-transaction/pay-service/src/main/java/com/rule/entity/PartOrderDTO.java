package com.rule.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PartOrderDTO implements Serializable {

    private Integer oid;
    private Integer skuNo;
    private Integer quantity;
    private BigDecimal price;
    private Integer entryId;
    private LocalDateTime entryDatetime;
}
