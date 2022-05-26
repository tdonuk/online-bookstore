package com.tdonuk.bookservice.model.repository;

import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.BookSource;
import com.tdonuk.bookservice.model.entity.Book;

import feign.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    List<Book> findAllByIsbn(String isbn);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthorsContains(Author author);

    List<Book> findAllByLanguage(String language);
    List<Book> findAllByTagsContains(String tag);
    
    boolean existsByIsbnAndSource(String isbn, BookSource source);
    Optional<Book> findByIsbnAndSource(String isbn, BookSource source);
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Book b set b.lastSeenInSearch = :date where b.id = :id")
    void updateLastSeen(@Param("id") long id, @Param("date") Date date);
}
