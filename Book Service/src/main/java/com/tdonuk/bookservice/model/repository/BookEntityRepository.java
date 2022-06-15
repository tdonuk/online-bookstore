package com.tdonuk.bookservice.model.repository;

import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookEntityRepository extends JpaRepository<SearchResultBookEntity, Long> {
    Optional<SearchResultBookEntity> findByAuthorsAndSourceAndTitleAndPublisher(String authors, String source, String title, String publisher);

    boolean existsByAuthorsAndSourceAndTitle(String authors, String source, String title);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update Book b set b.lastSeenInSearch = :date where b.id = :id")
    void updateLastSeen(@Param("id") long id, @Param("date") Date date);

}
