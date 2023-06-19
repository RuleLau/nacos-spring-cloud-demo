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
@Table(catalog = "pay", name = "user_balance")
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oid")
    private Long oid;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "entry_id")
    private Integer entryId;
    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;

    @Version
    @Column(name = "h_version")
    private Integer hVersion;

}
