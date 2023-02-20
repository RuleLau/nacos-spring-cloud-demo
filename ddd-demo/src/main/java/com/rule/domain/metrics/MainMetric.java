package com.rule.domain.metrics;

import java.util.ArrayList;
import java.util.List;

public abstract class MainMetric extends Metric{
    protected MainMetricType metricMainType;

    protected List<SubMetric> subMetrics = new ArrayList<>();

    public MainMetric(){
    }

    public void addSubMetric(SubMetric metric){
        subMetrics.add(metric);
    }

    @Override
    public String getName() {
        return metricMainType.getMetricName();
    }

    @Override
    public String getCode(){
        return metricMainType.getMetricCode();
    }


    @Override
    public double calculateScore() {
        double mainMetricScore = 0;
        for (Metric subMetric : subMetrics) {
            mainMetricScore = mainMetricScore + subMetric.calculateScore() * subMetric.getWeight();
        }
        return mainMetricScore;
    }

}
