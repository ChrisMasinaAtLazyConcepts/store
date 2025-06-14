package com.example.store.service;

import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import com.example.store.util.CustomUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        logger.info("Loading user by username from token: " + token);

        String username = token;
        Optional<User> user = null;
        if (username != null) {
            System.out.println("username: " + username);
            user = userRepository.findByUsername(username);

            if (user.isPresent()) {
                return new CustomUserDetails(user.get());
            }
        } else {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found");
        }
        return null;
    }
}
