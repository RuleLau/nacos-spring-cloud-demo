package com.rule.app.command;

import com.alibaba.cola.dto.Response;
import com.rule.client.dto.ATAMetricAddCmd;
import com.rule.domain.gateway.MetricGateway;
import com.rule.domain.metrics.techinfluence.ATAMetric;
import com.rule.domain.metrics.techinfluence.ATAMetricItem;
import com.rule.domain.metrics.techinfluence.InfluenceMetric;
import com.rule.domain.user.UserProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ATAMetricAddCmdExe {

    @Autowired
    private MetricGateway metricGateway;

    public Response execute(ATAMetricAddCmd cmd) {
        ATAMetricItem ataMetricItem = new ATAMetricItem();
        BeanUtils.copyProperties(cmd.getAtaMetricCO(), ataMetricItem);
        ataMetricItem.setSubMetric(new ATAMetric(new InfluenceMetric(new UserProfile(cmd.getAtaMetricCO().getOwnerId()))));
        metricGateway.save(ataMetricItem);
        return Response.buildSuccess();
    }

}
