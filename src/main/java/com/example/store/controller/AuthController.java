package com.example.store.controller;

import com.example.store.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling authentication requests.
 */
@RestController
@RequestMapping("/store")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenUtils tokenUtils;

    /**
     * Authenticates a user and returns a JWT token.
     * 
     * @param loginRequest The login request containing the username and password.
     * @return A ResponseEntity containing the JWT token if authentication is successful, or an error message if authentication fails.
     */
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            String token = tokenUtils.generateToken(authentication.getName());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
    }

    /**
     * Request body for login requests.
     */
    static class LoginRequest {
        private String username;
        private String password;

      
        public String getUsername() {
            return username;
        }

      
        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}