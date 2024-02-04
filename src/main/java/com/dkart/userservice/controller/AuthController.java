package com.dkart.userservice.controller;

import com.dkart.userservice.dto.CustomErrorResponse;
import com.dkart.userservice.dto.CustomSuccessResponse;
import com.dkart.userservice.dto.AuthRequest;
import com.dkart.userservice.dto.UserDTO;
import com.dkart.userservice.entity.UserCredential;
import com.dkart.userservice.exceptions.UnAuthorizedUserException;
import com.dkart.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private CustomSuccessResponse customSuccessResponse;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, ModelMapper modelMapper) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
      //  this.customSuccessResponse = customSuccessResponse;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addnewUser(@Valid @RequestBody UserCredential user){
        UserCredential userCredential = authService.saveUser(user);
        UserDTO userDTO= modelMapper.map(userCredential,UserDTO.class);
        customSuccessResponse = CustomSuccessResponse.builder()
                .status("SUCCESS")
                .data(userDTO)
                .message("User Credentials have been saved successfully").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(customSuccessResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<Object> getToken(@Valid @RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()) {
            String token = authService.generateToken(authenticate);
            customSuccessResponse=CustomSuccessResponse.builder()
                    .status("SUCCESS")
                    .data("Token:"+token)
                    .message("Token has been successfully generated").build();
            return ResponseEntity.status(HttpStatus.CREATED).body(customSuccessResponse);
        }else{
            throw new UnAuthorizedUserException("Invalid Access");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@Valid @RequestParam("token") String token){
        authService.validateToken(token);
        customSuccessResponse=CustomSuccessResponse.builder()
                .status("SUCCESS")
                .message("Token is valid").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(customSuccessResponse);
    }
}
