package com.rule.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "schedule_config", schema = "test")
public class ScheduleConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;

    /**
     * 用户名
     */
    @Column(name = "config_code")
    private String configCode;

    /**
     * 密码
     */
    @Column(name = "seconds")
    private Integer seconds;


}
