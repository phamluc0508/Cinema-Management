package com.cinema_management.user.configuration;

import com.cinema_management.user.enums.Role;
import com.cinema_management.user.model.User;
import com.cinema_management.user.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepo userRepo){
        return args -> {
            if(userRepo.findByUsername("admin").isEmpty()){
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userRepo.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            };
        };
    }
}
