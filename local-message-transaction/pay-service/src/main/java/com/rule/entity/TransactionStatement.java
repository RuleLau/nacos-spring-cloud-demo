package com.rule.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;

/*
 * 交易流水表
 * */
@Entity
@Data
@Table(catalog = "pay", name = "transaction_statement")
public class TransactionStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Long oid;

    @Column(name = "order_no")
    private Integer orderNo;

    @Column(name = "pay_status")
    private String payStatus;

    @Column(name = "entry_id")
    private Integer entryId;
    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;

    @Version
    @Column(name = "h_version")
    private Integer hVersion;

}
