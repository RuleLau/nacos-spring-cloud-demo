package com.rule.domain.metrics.techinfluence;

import com.rule.domain.metrics.MainMetric;
import com.rule.domain.metrics.SubMetric;
import com.rule.domain.metrics.SubMetricType;

public class ATAMetric extends SubMetric {

    public ATAMetric(){
        this.subMetricType = SubMetricType.ATA;
    }

    public ATAMetric(MainMetric parent) {
        this.parent = parent;
        parent.addSubMetric(this);
        this.subMetricType = SubMetricType.ATA;
    }

    @Override
    public double getWeight() {
        return  parent.getMetricOwner().getWeight().getUnanimousWeight();
}
