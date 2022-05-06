package com.tdonuk.bookservice.comm;

import com.tdonuk.bookservice.model.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("USER-SERVICE")
public interface UserClient {

    @GetMapping(value = "api/user/{id}", consumes = "application/json", produces = "application/json", headers = {})
    UserDTO getUser(@PathVariable(name = "id") long id, @RequestHeader(value = "Authorization", required = true) String token);


}
