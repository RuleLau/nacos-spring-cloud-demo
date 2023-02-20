package com.rule.domain.gateway;

import com.rule.domain.metrics.MetricItem;

public interface MetricGateway {

    public void save(MetricItem metricItem);
/*    public List<SubMetric> listByTechContribution(String userId);
    public List<SubMetric> listByTechInfluence(String userId);
    public BugMetric getBugMetric(String userId);
    public AppMetric getAppMetric(String userId);*/

}
