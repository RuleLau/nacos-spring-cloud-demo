package com.rule.service;

import com.rule.repository.TaskOdometerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TaskOdometerService {

    @Resource
    private TaskOdometerRepository taskOdometerRepository;


    @Transactional(rollbackFor = Exception.class)
    public Integer getTaskNo(String odometerName) {
        taskOdometerRepository.updateTaskNo(odometerName);
        return taskOdometerRepository.selectTaskNo(odometerName);
    }
}
