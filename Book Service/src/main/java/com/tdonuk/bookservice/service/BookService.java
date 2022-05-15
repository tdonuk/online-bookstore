package com.tdonuk.bookservice.service;

import com.tdonuk.bookservice.model.Author;
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
            if(! exists(book.getIsbn())) {
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
    
    public List<Book> saveMultiple(List<Book> books) {
    	List<Book> toSave = new ArrayList<Book>();
    	
    	try {
        	for(Book book : books) {
        		if(null != book.getIsbn() && !"".equals(book.getIsbn())) {
        			if(! exists(book.getIsbn())) { // if does not exist, save it
        				Date now = new Date();
        				
            			book.setCreationDate(now);
            			book.setLastSeenInSearch(now);
            			toSave.add(book);
        			}
        			else { // if exists, give the id and return it
        				Book b = findByIsbn(book.getIsbn());
        				
        				book.setId(b.getId());
        				book.setCreationDate(b.getCreationDate());
        				book.setLastSeenInSearch(updateLastSeen(book).getLastSeenInSearch());
        			}
        		}
        	}
    	} catch(Exception e) {
    		System.err.println(e.getMessage());
    	}
    	
    	bookRepository.saveAll(toSave);
    	
    	return Collections.unmodifiableList(books);
    }
    
    @Transactional
    public Book updateLastSeen(Book book) {
    	if(exists(book.getIsbn()) && 0 != book.getId()) {
    		EntityManager em = emf.createEntityManager();
        	
        	em.getTransaction().begin();
        	book = em.find(Book.class, book.getId());
        	
        	book.setLastSeenInSearch(new Date());
        	
        	em.getTransaction().commit();
        	
        	return book;
    	}
    	return null;
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

    public Book findByIsbn(final String isbn) {
        if (exists(isbn)) {
            return bookRepository.findByIsbn(isbn).orElse(null);
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

}
