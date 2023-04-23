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
@Table(name = "task", schema = "test")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;

    private int taskNo;

    @Column(name = "total_count")
    private int totalCount;
    @Column(name = "pending_count")
    private int pendingCount;

    @Column(name = "success_count")
    private int successCount;


    @Column(name = "fail_count")
    private int failCount;

    /**
     * Pending, PROCESSING, FAILED, FINISHED
     * 等待，处理中，失败，结束
     */
    @Column(name = "task_status")
    private String taskStatus;
}
