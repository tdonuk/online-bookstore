package com.tdonuk.bookservice.service;

import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book save(final Book book) {
        if(null != book.getIsbn()) {
            if(! exists(book.getIsbn())) {
                book.setCreationDate(new Date());
               return bookRepository.save(book);
            }
            else {
                return null;
            }
        }
        else {
            return bookRepository.save(book);
        }
    }

    public Book delete(final long id) {
        if(bookRepository.existsById(id)) {
            Book deleted = bookRepository.getById(id);
            bookRepository.deleteById(id);

            return deleted;
        }
        else return null;
    }

    public Book findById(final long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book findByIsbn(final String isbn) {
        if (exists(isbn)) {
            bookRepository.findByIsbn(isbn).orElse(null);
        }
        return null;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findByTitle(final String title) {
        return bookRepository.findAllByTitle(title);
    }

    public List<Book> findAllByAuthor(final Author author) {
        return bookRepository.findAllByAuthorsContains(author);
    }

    public List<Book> findAllByLanguage(final String language) {
        return bookRepository.findAllByLanguage(language);
    }

    public List<Book> findAllByTag(final String tag) {
        return bookRepository.findAllByTagsContains(tag);
    }

    public boolean exists(final String isbn) {
        return bookRepository.existsByIsbn(isbn) ? true : false;
    }

}
