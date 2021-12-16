package com.rule.algorithm;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class ShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        List<String> dataSources = new ArrayList<>(collection);
        long index = preciseShardingValue.getValue() % 4 % 2;
        String dataSource = dataSources.get((int) index);
        log.info("数据库下标 index: " + index + ", 数据源为 ：" + dataSource);
        return dataSource;
    }
}
