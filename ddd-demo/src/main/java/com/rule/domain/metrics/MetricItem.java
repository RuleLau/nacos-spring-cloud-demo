package com.rule.domain.metrics;

import com.alibaba.cola.domain.EntityObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.rule.domain.user.UserProfile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class MetricItem extends EntityObject implements Measurable {

    @JSONField(serialize = false)
    private SubMetric subMetric;

    /**
     * The owner of this MetricItem
     */
    @JSONField(serialize = false)
    private UserProfile metricOwner;

    public void setSubMetric(SubMetric subMetric){
        this.subMetric = subMetric;
        this.metricOwner = subMetric.getMetricOwner();
    }
    /**
     * 将度量项的转成JSON
     * @return
     */
    public String toJsonString() {
        String jsonStr = JSON.toJSONString(this, com.rule.domain.metrics.JSONPropertyFilter.singleton);
        log.debug("\n From : " + this + " \n To: " + jsonStr);
        return jsonStr;
    }

}
