package com.tdonuk.scraper.comm;

import java.util.List;
import java.util.Set;

import com.tdonuk.scraper.model.BookCard;
import com.tdonuk.scraper.model.SearchResultBookEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.tdonuk.scraper.model.entity.Book;

@FeignClient("BOOK-SERVICE")
public interface BookServiceClient {
    @PostMapping(value = "api/book/save/multiple", consumes = "application/json", produces = "application/json")
    List<BookCard> saveBooks(@RequestBody List<BookCard> books, @RequestHeader(value = "Authorization", required = false) String token);

}
