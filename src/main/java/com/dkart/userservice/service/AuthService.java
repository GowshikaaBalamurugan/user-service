package com.dkart.userservice.service;

import com.dkart.userservice.entity.UserCredential;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthService {
    void createDefaultAdmin();
    void registerAdmin(String username, String password);
    String saveUser(UserCredential userCredential);
    void validateToken(String token);

    String generateToken(Authentication authenticate);
}
