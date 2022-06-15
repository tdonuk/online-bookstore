package com.tdonuk.userservice.service;

import com.tdonuk.userservice.model.entity.SearchResultBookEntity;
import com.tdonuk.userservice.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService service;

    @Test
    void canGetUser() {
        UserEntity e = service.findById(556);

        System.out.println(e);
    }

    @Test
    void canAddToFavourites() {
        SearchResultBookEntity e = new SearchResultBookEntity();


    }
}
