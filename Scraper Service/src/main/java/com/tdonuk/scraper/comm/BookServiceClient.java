package com.tdonuk.scraper.comm;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.tdonuk.scraper.model.entity.Book;

@FeignClient("BOOK-SERVICE")
public interface BookServiceClient {
    @PutMapping(value = "api/book/save/multiple", consumes = "application/json", produces = "application/json")
    List<Book> saveBooks(@RequestBody List<Book> books, @RequestHeader(value = "Authorization", required = false) String token);
}
