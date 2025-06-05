package com.example.userservice;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDataInitializer {
    @Bean
    CommandLineRunner initUsers(UserRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new User("client1", "pass1", "client1@email.com", "0711000001", User.Role.CLIENT));
                repo.save(new User("employee1", "pass2", "employee1@email.com", "0722000002", User.Role.EMPLOYEE));
                repo.save(new User("manager1", "pass3", "manager1@email.com", "0733000003", User.Role.MANAGER));
                repo.save(new User("admin", "adminpass", "admin@email.com", "0744000004", User.Role.ADMIN));
            }
        };
    }
}
