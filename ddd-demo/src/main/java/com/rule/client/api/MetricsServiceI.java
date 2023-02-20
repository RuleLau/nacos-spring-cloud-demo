package com.rule.client.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.rule.client.dto.ATAMetricAddCmd;
import com.rule.client.dto.clientobject.ATAMetricCO;
import com.rule.client.dto.query.ATAMetricQry;

public interface MetricsServiceI {

    Response addATAMetric(ATAMetricAddCmd cmd);
    public MultiResponse<ATAMetricCO> listATAMetrics(ATAMetricQry ataMetricQry);
}
