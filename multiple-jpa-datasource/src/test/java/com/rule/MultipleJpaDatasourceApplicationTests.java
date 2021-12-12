package com.rule;

import com.rule.model.User;
import com.rule.repository.master.MasterRepository;
import com.rule.repository.slave.SlaveRepository;
import com.rule.repository.third.ThirdRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
class MultipleJpaDatasourceApplicationTests {

    /*
     * We will be using mysql databases we configured in our properties file for our tests
     * Make sure your datasource connections are correct otherwise the test will fail
     * */
    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private SlaveRepository slaveRepository;
    @Autowired
    private ThirdRepository thirdRepository;

    private static final User user = new User();

    @Before
    public void initializeDataObjects() {
        user.setPassword("1111");
        user.setAuthorities("xxxx");
    }

    @Test
    public void shouldSaveToMasterDB() {
        User user = new User();
        user.setId(2);
        user.setPassword("1111");
        user.setAuthorities("xxxx");
        user.setUsername("masterxxx");
        User user1 = masterRepository.save(user);
        Optional<User> userOptional = masterRepository.findById(user1.getId());
        assertTrue(userOptional.isPresent());
    }

    @Test
    public void shouldSaveToSlaveDB() {
        User user = new User();
        user.setId(2);
        user.setPassword("1111");
        user.setAuthorities("xxxx");
        user.setUsername("slavexxx");
        User user1 = slaveRepository.save(user);
        Optional<User> userOptional = slaveRepository.findById(user1.getId());
        assertTrue(userOptional.isPresent());
    }

    @Test
    public void shouldSaveToThirdDB() {
        User user = new User();
        user.setId(2);
        user.setPassword("1111");
        user.setAuthorities("xxxx");
        user.setUsername("thirdxxx");
        User user1 = thirdRepository.save(user);
        Optional<User> userOptional = thirdRepository.findById(user1.getId());
        assertTrue(userOptional.isPresent());
    }

}
