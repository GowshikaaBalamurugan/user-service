package com.dkart.userservice.service;

import com.dkart.userservice.entity.UserCredential;
import org.springframework.security.core.Authentication;

public interface AuthService {
    void createDefaultAdmin();
    void registerAdmin(String username, String password,String email);
    UserCredential saveUser(UserCredential userCredential);
    void validateToken(String token);

    String generateToken(Authentication authenticate);
}
