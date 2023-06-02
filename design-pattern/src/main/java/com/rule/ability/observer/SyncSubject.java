package com.rule.ability.observer;

import java.util.List;

/*
 * Observer 订阅的主题
 * */
public interface SyncSubject {

    void addDataSyncService(DataSync dataSync);

    void triggerSync(List<Integer> skuNoList);

}
