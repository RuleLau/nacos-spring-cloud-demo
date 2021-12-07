package com.rule.job.handler;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JobService {


    @XxlJob("test")
    public ReturnT<String> test(String param) {
//        String jobParam = XxlJobHelper.getJobParam();

        System.out.println(new Date() + ":" + param);
        return ReturnT.SUCCESS;
    }

    /*@XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) {
        System.out.println(new Date() + param);
        return ReturnT.SUCCESS;
    }*/

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDateTime(new Date()));
    }
}
