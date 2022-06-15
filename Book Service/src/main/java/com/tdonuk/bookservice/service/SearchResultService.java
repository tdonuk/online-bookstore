package com.tdonuk.bookservice.service;

import com.tdonuk.bookservice.comm.UserClient;
import com.tdonuk.bookservice.model.entity.Book;
import com.tdonuk.bookservice.model.entity.SearchResultBookEntity;
import com.tdonuk.bookservice.model.entity.UserEntity;
import com.tdonuk.bookservice.model.repository.BookEntityPageableRepository;
import com.tdonuk.bookservice.model.repository.BookEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

@Service
public class SearchResultService {
    @Autowired
    private BookEntityRepository bookEntityRepository;

    @Autowired
    private BookEntityPageableRepository bookEntityPageableRepository;

    @Transactional
    public List<SearchResultBookEntity> saveMultiple(List<SearchResultBookEntity> books) throws Exception {

        List<SearchResultBookEntity> processed = new ArrayList<>();

        for(SearchResultBookEntity book : books) {
            try {
                SearchResultBookEntity b = findUnique(book.getTitle(), book.getSource(), book.getAuthors(), book.getPublisher());

                if(b == null) {
                    book.setLastSeenInSearch(new Date());

                    processed.add(bookEntityRepository.save(book));
                }

                else {
                    b.setLastSeenInSearch(new Date());

                    bookEntityRepository.updateLastSeen(b.getId(), book.getLastSeenInSearch());

                    processed.add(b);
                }
            } catch(Exception e) {
                System.out.println("ERROR WHILE SAVING BOOK: " + e.getMessage());
                continue;
            }
        }

        return processed;
    }

    public Page<SearchResultBookEntity> findLastSearched(Pageable pageable) {
        return bookEntityRepository.findAll(pageable);
    }

    public Page<SearchResultBookEntity> findMostLiked(Pageable pageable) {
        return bookEntityPageableRepository.findMostLiked(pageable);
    }

    private SearchResultBookEntity findUnique(String title, String source, String authors, String publisher) {
        return bookEntityRepository.findByAuthorsAndSourceAndTitleAndPublisher(authors, source, title, publisher).orElse(null);
    }

}
