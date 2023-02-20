package com.rule.domain.metrics;

import com.rule.domain.user.UserProfile;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class SubMetric extends Metric {

    protected SubMetricType subMetricType;

    protected MainMetric parent;

    @Getter
    private List<MetricItem> metricItemList = new ArrayList<>();

    public SubMetric(){

    }

    public void setParent(MainMetric parent){
        this.parent = parent;
        this.metricOwner = parent.metricOwner;
        parent.addSubMetric(this);
    }

    /**
     * 添加度量项
     * @param metricItem
     */
    public void addMetricItem(MetricItem metricItem){
        metricItemList.add(metricItem);
    }


    @Override
    public String getName() {
        return subMetricType.getMetricSubTypeName();
    }

    @Override
    public String getCode(){
        return subMetricType.getMetricSubTypeCode();
    }

    @Override
    public double calculateScore() {
        double subMetricScore = 0;
        for (MetricItem metricItem : metricItemList) {
            subMetricScore = subMetricScore + metricItem.calculateScore();
        }
        return subMetricScore;
    }

    @Override
    public UserProfile getMetricOwner(){
        return parent.getMetricOwner();
    }

}
