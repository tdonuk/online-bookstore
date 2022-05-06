package com.tdonuk.userservice.service;

import com.tdonuk.userservice.model.entity.UserEntity;
import com.tdonuk.userservice.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    public UserDetailsServiceImpl() {}

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        this.user = userRepository.findByEmail(email).orElse(null);
        return new UserDetailsImpl(user);
    }

    public void updateUserLastLoginDate(final long id, final Date date) {
        userRepository.updateLastLoginDate(id, date);
    }
}
