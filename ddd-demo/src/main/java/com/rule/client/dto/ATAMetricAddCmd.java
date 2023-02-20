package com.rule.client.dto;

import com.rule.client.dto.clientobject.ATAMetricCO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ATAMetricAddCmd {

    @NotNull
    private ATAMetricCO ataMetricCO;
}
