package com.rule.controller;

import com.rule.po.Manager;
import com.rule.service.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private StoreService storeService;

    @GetMapping("/load/em")
    public long batchLoadByEm(@RequestParam("dataSize") int dataSize) {
        List<Manager> managerList = getManagerList(dataSize);

        long startTime = System.currentTimeMillis();

        storeService.loadToDbByEm(managerList);

        return System.currentTimeMillis() - startTime;
    }

    private List<Manager> getManagerList(int dataSize) {
        List<Manager> managerList = new ArrayList<>(dataSize);
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < dataSize; i++) {
            Manager manager = new Manager();
            manager.setUserid(currentTimeMillis + i);
            manager.setName("name-" + manager.getUserid());
            manager.setProcessStatus("PENDING");
            managerList.add(manager);
        }
        return managerList;
    }

    @GetMapping("/load/jdbc")
    public long batchLoadByJdbc(@RequestParam("dataSize") int dataSize) throws Exception{
        List<Manager> managerList = getManagerList(dataSize);

        long startTime = System.currentTimeMillis();

        storeService.loadToDbByJdbc(managerList);

        return System.currentTimeMillis() - startTime;
    }

}
