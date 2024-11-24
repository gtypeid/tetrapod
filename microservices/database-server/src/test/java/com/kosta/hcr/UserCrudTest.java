package com.kosta.hcr;

import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.DatabaseAppConfig;
import com.kosta.databaseserver.DatabaseServerApplication;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@Import(DatabaseAppConfig.class)
@SpringBootTest(classes = DatabaseServerApplication.class)
public class UserCrudTest {

    @Autowired
    UserRepository userRepository;

    List<User> users;
    String[] userNames;
    Map<String, Long> userIdMap;

    @BeforeEach
    public void setUp() {
        userNames = new String[]{
                "tester0",
                "tester1",
                "tester2"
        };

        users = new ArrayList<>();
        userIdMap = new HashMap<>();
        for (String name : userNames) {
            User user = new User();
            user.setName(name);
            users.add(user);
        }

        for(int i = 0; i < userNames.length; ++i){
            User createdUser = userRepository.create(users.get(i));
            userIdMap.put(createdUser.getName(), createdUser.getId());
        }
    }

    @Test
    public void createUser(){

        for(int i = 0; i < userNames.length; ++i){
            User createdUser = userRepository.create(users.get(i));

            assertNotNull(createdUser, "User Not Null");
            assertEquals(userNames[i], createdUser.getName(), "User name should match");
        }
    }

    @Test
    public void readUser(){
        for (String userName : userNames) {
            Long userId = userIdMap.get(userName);

            User readUser = userRepository.read(userId);

            assertNotNull(readUser, "User Not Null");
            assertEquals(userName, readUser.getName(), "User name should match");
        }
    }

    @Test
    public void updateUser(){
        for (String userName : userNames) {
            Long userId = userIdMap.get(userName);
            User newUser = new User();
            newUser.setName("updateUser");

            User updateUser = userRepository.update(userId, newUser);

            assertNotNull(updateUser, "User Not Null");
            assertNotEquals(userName, updateUser.getName(), "User name not match");
        }
    }

    @Test
    public void deleteUser(){
        for (String userName : userNames) {
            Long userId = userIdMap.get(userName);
            boolean isDelete = userRepository.delete(userId);
            assertTrue(isDelete);
        }
    }
}

