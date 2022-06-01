package com.tdonuk.userservice.model.repository;

import com.tdonuk.userservice.model.entity.SearchResultBookEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface BookEntityRepository extends JpaRepository<SearchResultBookEntity, Long> {
    Optional<SearchResultBookEntity> findByAuthorsAndSourceAndTitleAndPublisher(String authors, String source, String title, String publisher);

    boolean existsByAuthorsAndSourceAndTitle(String authors, String source, String title);

}
