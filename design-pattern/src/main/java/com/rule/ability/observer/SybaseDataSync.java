package com.rule.ability.observer;

import java.util.Arrays;
import java.util.List;

public class SybaseDataSync implements DataSync {

    public SybaseDataSync(PartSync partSync) {
        partSync.addDataSyncService(this);
    }

    @Override
    public void sync(List<Integer> skuNoList) {
        System.out.println("Sybase start to sync skuNoList: " + Arrays.toString(skuNoList.toArray()));
    }
}
