package com.dkart.userservice.service;

import com.dkart.userservice.entity.UserCredential;
import com.dkart.userservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService{

  private final PasswordEncoder passwordEncoder;
  private final UserCredentialRepository userCredentialRepository;
  private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(PasswordEncoder passwordEncoder, UserCredentialRepository userCredentialRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userCredentialRepository = userCredentialRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void createDefaultAdmin() {
        registerAdmin("admin","adminpwd");
    }

    @Override
    public void registerAdmin(String username, String password) {
//        userCredential.setUsername(username);
//        userCredential.setUsername(passwordEncoder.encode(password));
//        userCredential.setRole("ADMIN");
        userCredentialRepository.save(new UserCredential(username,passwordEncoder.encode(password),"ADMIN"));
    }


    @Override
    public String saveUser(UserCredential userCredential) {
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        if(Objects.equals(userCredential.getRole().toUpperCase(), "USER")){
        userCredentialRepository.save(userCredential);
        return "User creds added to the System";
        }
        else{
            throw new IllegalArgumentException("Invalid role to register..");
        }

    }

    /*@Override
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return jwtService.generateToken(username,authorities);
    }*/

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    @Override
    public String generateToken(Authentication authenticate) {
        String role= String.valueOf(authenticate.getAuthorities().stream().toList().get(0));
        return jwtService.generateToken(authenticate.getName(),role);
    }
}
