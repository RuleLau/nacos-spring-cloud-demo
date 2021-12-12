package com.rule.repository.master;

import com.rule.model.User;
import org.springframework.data.repository.CrudRepository;

public interface MasterRepository extends CrudRepository<User, Integer> {
}
