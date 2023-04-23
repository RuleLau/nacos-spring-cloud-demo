package com.rule.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class TaskEvent extends ApplicationEvent {

    private Integer taskNo;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TaskEvent(Object source, Integer taskNo) {
        super(source);
        this.taskNo = taskNo;
    }
}
