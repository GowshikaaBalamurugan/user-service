package com.dkart.userservice.config;

import com.dkart.userservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer implements ApplicationRunner {
    private final AuthService authService;

    @Autowired
    public DefaultAdminInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        authService.createDefaultAdmin();
    }
}
