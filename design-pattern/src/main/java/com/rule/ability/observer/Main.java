package com.rule.ability.observer;

import com.google.common.collect.Lists;

public class Main {


    public static void main(String[] args) {

        PartSync partSync = new PartSync();

        new MySQLDataSync(partSync);
        new SybaseDataSync(partSync);

        partSync.triggerSync(Lists.newArrayList(1, 2, 3, 4, 5));
    }
}
