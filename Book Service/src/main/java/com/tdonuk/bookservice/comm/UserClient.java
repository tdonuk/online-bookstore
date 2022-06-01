package com.tdonuk.bookservice.comm;

import com.tdonuk.bookservice.model.UserDTO;
import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import com.tdonuk.bookservice.model.entity.UserEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient("USER-SERVICE")
public interface UserClient {
    @GetMapping(value = "api/user/{id}", consumes = "application/json", produces = "application/json", headers = {})
    UserEntity getUser(@RequestHeader(value = "Authorization") String token);

    @GetMapping(value = "api/user/currentUser", produces = "application/json", headers = {})
    UserEntity getLoggedUser(@RequestHeader(value = "Authorization") String token);

    @PostMapping(value = "api/user/detail/favourites")
    void addBooksToFavourites(@RequestHeader(value = "Authorization") String token, @RequestBody List<SearchResultBookEntity> books);

    @PostMapping(value = "api/user/detail/history")
    void addBooksToHistory(@RequestHeader(value = "Authorization") String token, @RequestBody List<SearchResultBookEntity> books);
}
