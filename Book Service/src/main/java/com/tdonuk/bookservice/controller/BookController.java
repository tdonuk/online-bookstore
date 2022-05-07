package com.tdonuk.bookservice.controller;

import com.tdonuk.bookservice.comm.UserClient;
import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.Name;
import com.tdonuk.bookservice.model.UserDTO;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/")
public class BookController {
    @Autowired
    BookService bookService;

    @Autowired
    UserClient userClient;

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

    @GetMapping("/last")
    public ResponseEntity<?> books() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/find")
    public ResponseEntity<?> findByAuthor(@RequestParam String firstname, @RequestParam String lastname) {
        List<Book> books = bookService.findAllByAuthor(new Author(new Name(firstname, lastname)));

        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{id}/details")
    public ResponseEntity<?> getDetails(@PathVariable long id) {
    	try {
    		return ResponseEntity.ok(bookService.findById(id));
    	} catch(Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> saveMultiple(@RequestBody List<Book> books) {
    	try {
    		books.forEach(book -> bookService.save(book));
    		return ResponseEntity.ok().build();
    	} catch(Exception e) {
    		return ResponseEntity.badRequest().body(e.getMessage());
    	}
    }
    

}
