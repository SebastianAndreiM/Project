package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class OrderDataInitializer {
    @Bean
    CommandLineRunner initOrders(OrderRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Order(1L, 1L, 1L, "42", "Alb", 1, 499.99, LocalDateTime.now()));
                repo.save(new Order(2L, 2L, 2L, "43", "Negru", 2, 350.00, LocalDateTime.now().minusDays(1)));
            }
        };
    }
}
