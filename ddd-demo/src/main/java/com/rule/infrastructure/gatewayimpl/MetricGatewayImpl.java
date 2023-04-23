package com.rule.infrastructure.gatewayimpl;

import com.rule.client.dto.domainevent.MetricItemCreatedEvent;
import com.rule.domain.gateway.MetricGateway;
import com.rule.domain.metrics.MetricItem;
import com.rule.infrastructure.common.event.DomainEventPublisher;
import com.rule.infrastructure.convertor.MetricConvertor;
import com.rule.infrastructure.gatewayimpl.database.MetricMapper;
import com.rule.infrastructure.gatewayimpl.database.dataobject.MetricDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MetricGatewayImpl implements MetricGateway {

    @Resource
    private MetricMapper metricMapper;

    @Resource
    private DomainEventPublisher domainEventPublisher;
    @Override
    public void save(MetricItem metricItem) {
        MetricDO metricDO = MetricConvertor.toDataObject(metricItem);

        metricMapper.create(metricDO);

        log.debug("AutoGeneratedId: "+metricDO.getId());
        MetricItemCreatedEvent metricItemCreatedEvent = new MetricItemCreatedEvent();
        metricItemCreatedEvent.setId(metricDO.getId());
        metricItemCreatedEvent.setUserId(metricDO.getUserId());
        metricItemCreatedEvent.setMainMetricType(metricDO.getMainMetric());
        domainEventPublisher.publish(metricItemCreatedEvent);
    }
}