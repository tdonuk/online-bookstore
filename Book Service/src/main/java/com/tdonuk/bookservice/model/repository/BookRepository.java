package com.tdonuk.bookservice.model.repository;

import com.tdonuk.bookservice.model.Author;
import com.tdonuk.bookservice.model.entity.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
    Optional<Book> findByIsbn(String isbn);

    List<Book> findAllByTitle(String title);

    List<Book> findAllByAuthorsContains(Author author);

    List<Book> findAllByLanguage(String language);
    List<Book> findAllByTagsContains(String tag);
}
