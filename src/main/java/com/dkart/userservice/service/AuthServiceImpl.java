package com.dkart.userservice.service;

import com.dkart.userservice.entity.UserCredential;
import com.dkart.userservice.exceptions.UnAuthorizedUserException;
import com.dkart.userservice.exceptions.UserAlreadyExistsException;
import com.dkart.userservice.repository.UserCredentialRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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
        registerAdmin("admin","Adminpwd@123","admin123@gmail.com");
    }


    public void registerAdmin(String username, String password,String email) {
//        userCredential.setUsername(username);
//        userCredential.setUsername(passwordEncoder.encode(password));
//        userCredential.setRole("ADMIN");
        userCredentialRepository.save(new UserCredential(username,email,passwordEncoder.encode(password),"ADMIN"));
    }


    @Override
    public UserCredential saveUser(UserCredential userCredential) {
        Optional<UserCredential> user=userCredentialRepository.findByUsername(userCredential.getUsername());
        if(user.isPresent()) throw new UserAlreadyExistsException("User already Exists.");
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        if(Objects.equals(userCredential.getRole().toUpperCase(), "USER")){
        userCredentialRepository.save(userCredential);
        return userCredential;
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
        try {
            jwtService.validateToken(token);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
        throw new UnAuthorizedUserException("Invalid Token");
    }
    }

    @Override
    public String generateToken(Authentication authenticate) {
        String role= String.valueOf(authenticate.getAuthorities().stream().toList().get(0));
        return jwtService.generateToken(authenticate.getName(),role);
    }
}
