package com.rule.entity;

import lombok.Data;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Access(AccessType.FIELD)
@Table(catalog = "mq", name = "event_message")
public class EventMessage implements Serializable {

    @Id
    @Column(name = "message_id")
    private String messageId;
    @Column(name = "event_name")
    private String eventName;
    @Column(name = "message_status")
    private String messageStatus;
    @Column(name = "payload")
    private String payload;
    @Column(name = "entry_id")
    private Integer entryId;
    @Column(name = "entry_datetime")
    private LocalDateTime entryDatetime;
    @Version
    @Column(name = "h_version")
    private Integer hVersion;
}
