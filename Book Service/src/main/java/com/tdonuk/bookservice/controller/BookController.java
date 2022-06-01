package com.tdonuk.bookservice.controller;

import com.tdonuk.bookservice.comm.UserClient;
import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.Name;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import com.tdonuk.bookservice.model.entity.UserEntity;
import com.tdonuk.bookservice.service.BookService;
import com.tdonuk.bookservice.service.SearchResultService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/book/")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private SearchResultService searchResultService;

    @Autowired
    private UserClient userClient;

    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody Book book) {
        Book saved = bookService.save(book);

        if(null != saved && 0 != saved.getId()) {
            return ResponseEntity.ok("Kitap kaydı başarıyla yapıldı. ID: " + saved.getId());
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Book deleted = bookService.delete(id);

        if(null != deleted) {
            return ResponseEntity.ok("Kitap silme işlemi başarıyla yapıldı. ID: " + deleted.getId());
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/public/last")
    public ResponseEntity<?> books() {
    	Pageable page = PageRequest.of(0, 10, Sort.by("lastSeenInSearch").descending());
        return ResponseEntity.ok(bookService.findLast(page));
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByAuthor(@RequestParam String firstname, @RequestParam String lastname) {
        List<Book> books = bookService.findAllByAuthor(new Author(new Name(firstname, lastname)));

        return ResponseEntity.ok(books);
    }
    
    @GetMapping("public/{id}/details")
    public ResponseEntity<?> getDetails(@PathVariable long id) {
    	try {
    		return ResponseEntity.ok(bookService.findById(id));
    	} catch(Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }

    @GetMapping("public/search/last")
    public ResponseEntity<?> findLastSearchedItems() {
        try {
            Pageable p = PageRequest.of(0,20, Sort.by("lastSeenInSearch").descending());
            Page<SearchResultBookEntity> books = searchResultService.findLastSearched(p);

            return ResponseEntity.ok(books);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("public/search/mostLiked")
    public ResponseEntity<?> findMostLikedItems() {
        try {
            Pageable p = PageRequest.of(0,20);
            Page<SearchResultBookEntity> books = searchResultService.findMostLiked(p);

            return ResponseEntity.ok(books);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("save/multiple")
    public ResponseEntity<?> saveMultiple(@RequestBody List<SearchResultBookEntity> books, HttpServletRequest request) {
    	try {
            List<SearchResultBookEntity> bookEntities = searchResultService.saveMultiple(books);

            String authorization = request.getHeader("Authorization");

            if(Objects.nonNull(authorization) && !StringUtils.isEmpty(authorization)) {
                UserEntity user = userClient.getLoggedUser(authorization);
                userClient.addBooksToHistory(authorization, bookEntities);
            }

    		return ResponseEntity.ok(bookEntities);
    	} catch(Exception e) {
            e.printStackTrace();
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }

}
