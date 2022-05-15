package com.tdonuk.bookservice.controller;

import com.tdonuk.bookservice.comm.UserClient;
import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.Name;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/")
public class BookController {
    @Autowired
    private BookService bookService;

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
    
    @PutMapping("save/multiple")
    public ResponseEntity<?> saveMultiple(@RequestBody List<Book> books) {
    	try {
    		return ResponseEntity.ok(bookService.saveMultiple(books));
    	} catch(Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    

}
