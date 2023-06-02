package com.rule.ability.observer;

import java.util.Arrays;
import java.util.List;

public class MySQLDataSync implements DataSync {

    public MySQLDataSync(PartSync partSync) {
        partSync.addDataSyncService(this);
    }

    @Override
    public void sync(List<Integer> skuNoList) {
        System.out.println("MySQL start to sync skuNoList: " + Arrays.toString(skuNoList.toArray()));
    }
}
