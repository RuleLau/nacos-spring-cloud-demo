package com.rule.app.service;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.rule.app.command.ATAMetricAddCmdExe;
import com.rule.app.command.query.ATAMetricQryExe;
import com.rule.client.api.MetricsServiceI;
import com.rule.client.dto.ATAMetricAddCmd;
import com.rule.client.dto.clientobject.ATAMetricCO;
import com.rule.client.dto.query.ATAMetricQry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MetricsServiceImpl implements MetricsServiceI {

    @Resource
    private ATAMetricQryExe ataMetricQryExe;

    @Resource
    private ATAMetricAddCmdExe ataMetricAddCmdExe;

    @Override
    public Response addATAMetric(ATAMetricAddCmd cmd) {
        return ataMetricAddCmdExe.execute(cmd);
    }


    @Override
    public MultiResponse<ATAMetricCO> listATAMetrics(ATAMetricQry ataMetricQry) {
        return ataMetricQryExe.execute(ataMetricQry);
    }
}
