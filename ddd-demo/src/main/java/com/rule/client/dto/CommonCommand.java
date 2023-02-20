package com.rule.client.dto;

import com.alibaba.cola.dto.Command;
import lombok.Data;

@Data
public class CommonCommand extends Command {

    private String operator;

    private boolean needsOperator;


}
