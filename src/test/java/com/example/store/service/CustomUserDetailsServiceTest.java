package com.example.store.service;

import com.example.store.entity.User;
import com.example.store.repository.UserRepository;
import com.example.store.util.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("username");

        assertEquals(CustomUserDetails.class, result.getClass());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("username"));
    }
} 