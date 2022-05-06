package com.tdonuk.userservice;

import com.tdonuk.userservice.model.Name;
import com.tdonuk.userservice.model.UserRole;
import com.tdonuk.userservice.model.entity.UserEntity;
import com.tdonuk.userservice.service.UserService;
import com.tdonuk.userservice.util.UserValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.Date;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void createAccount() throws Exception {
        UserEntity user = new UserEntity();

        user.setAccountCreationDate(new Date());
        user.setUsername("tdonuk");
        user.setPassword("1234");
        user.setEmail("tdonuk@email.com");
        user.setName(new Name());
        user.getName().setFirstname("Taha");
        user.getName().setLastname("Dönük");
        user.setRole(UserRole.ADMIN);
        user.setBirthDate(Date.from(LocalDate.of(1999,1,1).atStartOfDay().toInstant(OffsetDateTime.now().getOffset())));

        userService.save(user);
    }
}
