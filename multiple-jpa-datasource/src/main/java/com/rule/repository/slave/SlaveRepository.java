package com.rule.repository.slave;

import com.rule.model.User;
import org.springframework.data.repository.CrudRepository;

public interface SlaveRepository extends CrudRepository<User, Integer> {
}
