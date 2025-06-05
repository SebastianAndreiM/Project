package org.example.storeservice;

import org.example.storeservice.model.Store;
import org.example.storeservice.repository.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreDataInitializer {
    @Bean
    CommandLineRunner initStores(StoreRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Store("Store Cluj", "Cluj-Napoca", "0740000000", "cluj@shoestore.ro", "img/logo_cluj.jpg"));
                repo.save(new Store("Store Bucuresti", "București", "0751000000", "bucuresti@shoestore.ro", "img/logo_bucuresti.jpg"));
                repo.save(new Store("Store Timisoara", "Timișoara", "0762000000", "timisoara@shoestore.ro", "img/logo_timisoara.jpg"));
            }
        };
    }
}
