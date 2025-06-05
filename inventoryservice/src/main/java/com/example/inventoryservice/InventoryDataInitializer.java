package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryDataInitializer {
    @Bean
    CommandLineRunner initInventories(InventoryRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Inventory(1L, 1L, "42", "Alb", 10));
                repo.save(new Inventory(1L, 2L, "43", "Negru", 7));
                repo.save(new Inventory(2L, 1L, "40", "Roșu", 5));
            }
        };
    }
}
