package com.tdonuk.userservice.service;

import com.tdonuk.userservice.constant.Errors;
import com.tdonuk.userservice.model.UserRole;
import com.tdonuk.userservice.model.entity.SearchResultBookEntity;
import com.tdonuk.userservice.model.entity.UserEntity;
import com.tdonuk.userservice.model.repository.BookEntityRepository;
import com.tdonuk.userservice.model.repository.UserRepository;
import com.tdonuk.userservice.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.tdonuk.userservice.constant.Errors.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookEntityRepository bookRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserEntity save(UserEntity entity) throws Exception {
        try {
            UserValidator.validate(entity);

            entity.setPassword(encoder.encode(entity.getPassword()));
            entity.setRole(UserRole.USER);
            entity.setAccountCreationDate(new Date());

            userRepository.save(entity);

            return entity;
        } catch(Exception e) {
            throw new Exception(UNKNOWN_ERROR.getMessage());
        }
    }

    public UserEntity update(UserEntity entity) throws Exception {
        try {
            userRepository.save(entity);

            return entity;
        } catch(Exception e) {
            throw new Exception(UNKNOWN_ERROR.getMessage());
        }
    }

    public UserEntity findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserEntity findByUsername(String username) {
        try {
            return userRepository.findByUsername(username).orElseThrow(() -> new Exception(USERNAME_NOT_FOUND.getMessage()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public UserEntity findByEmail(String email) {
    	try {
    		return userRepository.findByEmail(email).orElse(null);
    	} catch(Exception e) {
    		return null;
    	}
    }

    public void addToFavourites(String username, List<SearchResultBookEntity> books) throws Exception{
        UserEntity entity = userRepository.findByEmail(username).orElse(null);

        if(null == entity) {
            return;
        }

        List<SearchResultBookEntity> toUpdate = new ArrayList<>();

        for(SearchResultBookEntity book : books) {
            SearchResultBookEntity bookEntity = bookRepository.getById(book.getId());

            bookEntity.getFavouriters().add(entity);
            bookEntity.setFavouriteCount(bookEntity.getFavouriters().size());

            toUpdate.add(bookEntity);
        }

        entity.getFavourites().addAll(toUpdate);

        update(entity);
    }

    public void removeFromFavourites(String username, List<SearchResultBookEntity> books) throws Exception{
        UserEntity entity = userRepository.findByEmail(username).orElse(null);

        if(null == entity) {
            return;
        }

        List<SearchResultBookEntity> toUpdate = new ArrayList<>();

        for(SearchResultBookEntity book : books) {
            SearchResultBookEntity bookEntity = bookRepository.getById(book.getId());

            bookEntity.getFavouriters().remove(entity);
            bookEntity.setFavouriteCount(bookEntity.getFavouriters().size());

            toUpdate.add(bookEntity);
        }

        entity.getFavourites().removeAll(toUpdate);

        update(entity);
    }

    public void addToHistory(String username, List<SearchResultBookEntity> books) throws Exception {
        UserEntity entity = userRepository.findByEmail(username).orElse(null);

        if(null == entity) {
            return;
        }

        List<SearchResultBookEntity> toUpdate = new ArrayList<>();

        for(SearchResultBookEntity book : books) {
            SearchResultBookEntity bookEntity = bookRepository.getById(book.getId());

            bookEntity.getFavouriters().add(entity);

            toUpdate.add(bookEntity);
        }

        entity.getSearches().addAll(toUpdate);

        bookRepository.saveAll(books);

        update(entity);
    }

    public UserEntity delete(UserEntity entity) throws Exception {
        if(userRepository.existsById(entity.getUserId())) {
            UserEntity deleted = findById(entity.getUserId());

            userRepository.delete(entity);

            return deleted;
        }
        else throw new Exception(UNKNOWN_ERROR.getMessage());
    }

    public void updateLastLogin(long id, Date date) {
        if(userRepository.existsById(id)) {
            userRepository.updateLastLoginDate(id,date);
        }
    }

    public long count() {
        return userRepository.count();
    }

    public boolean exists(UserEntity entity) throws Exception{
        if(existsByEmail(entity.getEmail())) throw new Exception(EMAIL_CONFLICT.getMessage());
        else if(existsByUsername(entity.getUsername())) throw new Exception(USERNAME_CONFLICT.getMessage());

        else return false;
    }

    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
}
