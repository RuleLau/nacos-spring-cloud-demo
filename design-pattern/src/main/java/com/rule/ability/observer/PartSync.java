package com.rule.ability.observer;

import java.util.ArrayList;
import java.util.List;

public class PartSync implements SyncSubject {

    private List<DataSync> dataSyncs = new ArrayList<>();


    @Override
    public void addDataSyncService(DataSync dataSync) {
        dataSyncs.add(dataSync);
    }

    @Override
    public void triggerSync(List<Integer> skuNoList) {
        for (DataSync dataSync : dataSyncs) {
            dataSync.sync(skuNoList);
        }
    }
}
