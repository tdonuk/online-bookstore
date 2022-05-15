package com.tdonuk.bookservice.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tdonuk.bookservice.model.entity.Book;

public interface BookPagingRepository extends PagingAndSortingRepository<Book, Long> {
    Page<Book> findAll(Pageable pageable);
}
