package com.rule.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "manager", schema = "test")
public class Manager implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;

    private int taskNo;

    private Long userid;

    private String name;

    /**
     * Pending, PROCESSING, FAILED, SUCCESS
     * 等待，处理中，失败，成功
     */
    @Column(name = "process_status")
    private String processStatus;
}
