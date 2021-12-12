package com.rule.repository.third;

import com.rule.model.User;
import org.springframework.data.repository.CrudRepository;

public interface ThirdRepository extends CrudRepository<User, Integer> {
}
