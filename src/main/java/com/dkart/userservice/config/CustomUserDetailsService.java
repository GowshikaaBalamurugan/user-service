package com.dkart.userservice.config;

import com.dkart.userservice.entity.CustomUserDetails;
import com.dkart.userservice.entity.UserCredential;
import com.dkart.userservice.repository.UserCredentialRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    public CustomUserDetailsService(UserCredentialRepository userCredentialRepository, ModelMapper modelMapper) {
//        this.userCredentialRepository = userCredentialRepository;
//        this.modelMapper = modelMapper;
//    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential = userCredentialRepository.findByUsername(username);
        return userCredential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));

    }

}
