package com.tdonuk.bookservice.model.repository;

import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookEntityPageableRepository extends PagingAndSortingRepository<SearchResultBookEntity, Long> {
    Page<SearchResultBookEntity> findAll(Pageable pageable);

    @Query("select book from SearchResultBookEntity book order by book.favouriters.size desc")
    Page<SearchResultBookEntity> findMostLiked(Pageable pageable);
}
