package com.rule.dao;

import com.rule.entity.ScheduleConfigEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleConfigRepository extends CrudRepository<ScheduleConfigEntity, Integer> {

    ScheduleConfigEntity findByConfigCode(String configCode);

}
