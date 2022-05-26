package com.tdonuk.bookservice.service;

import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.BookSource;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.model.repository.BookPagingRepository;
import com.tdonuk.bookservice.model.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookPagingRepository bookPagingRepository;
    
    @PersistenceUnit
    private EntityManagerFactory emf;

    public Book save(final Book book) {
        if(null != book.getIsbn()) {
            if(! exists(book.getIsbn(), book.getSource())) {
            	Date now = new Date();
                book.setCreationDate(now);
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
    
    @Transactional
    public List<Book> saveMultiple(List<Book> books) throws Exception {
    	
    	List<Book> processed = new ArrayList<>();
    	
    	for(Book book : books) {
    		try {
        		Book b = findByIsbnAndSource(book.getIsbn(), book.getSource());
        		
        		if(b == null) {
        			
        			book.setCreationDate(new Date());
        			book.setLastSeenInSearch(new Date());
        			
        			b = save(book);
        			
        			processed.add(b);
        		}
        		
        		else {
        			updateLastSeenInSearch(b);
        			
        			processed.add(b);
        		}	
    		} catch(Exception e) {
    			System.out.println("ERROR WHILE SAVING BOOK: " + e.getMessage());
    			continue;
    		}
    	}
    	
    	return processed;
    }
    

    public Book delete(final long id) {
        if(bookRepository.existsById(id)) {
            Book deleted = bookRepository.findById(id).get();
            bookRepository.deleteById(id);

            return deleted;
        }
        else return null;
    }

    public Book findById(final long id) {
        return bookRepository.findById(id).orElse(null);
    }
    
    public void updateLastSeenInSearch(Book book) throws Exception{
    	try {
    		book.setLastSeenInSearch(new Date());
    		bookRepository.updateLastSeen(book.getId(), book.getLastSeenInSearch());
    	} catch(Exception e) {
    		throw e;
    	}
    }

    public Book findByIsbnAndSource(final String isbn, final BookSource source) throws Exception {
    	return bookRepository.findByIsbnAndSource(isbn, source).orElse(null);
    }
    
    public List<Book> findAllByIsbn(final String isbn) {
        if (exists(isbn)) {
            return bookRepository.findAllByIsbn(isbn);
        }
        return null;
    }

    public Page<Book> findLast(Pageable pageable) {
        return bookPagingRepository.findAll(pageable);
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
    
    public boolean exists(final String isbn, final BookSource source) {
    	return bookRepository.existsByIsbnAndSource(isbn, source);
    }

}
