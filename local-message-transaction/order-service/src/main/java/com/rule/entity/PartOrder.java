package com.rule.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(catalog = "order", name = "part_order")
public class PartOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Long oid;

    @Column(name = "sku_no")
    private Integer skuNo;

    @Column(name = "quantity")
    private Integer quantity;

    /*
     * 未支付/支付中/已完成
     * */
    @Column(name = "order_status")
    private String orderStatus;

    // 单价
    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "entry_id")
    private Integer entryId;
    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;

    @Version
    @Column(name = "h_version")
    private Integer hVersion;

}
