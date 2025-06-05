package org.example.storeservice.service;

import org.example.storeservice.model.Store;
import org.example.storeservice.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Pattern: Service Layer
public class StoreService {
    private final StoreRepository repo;

    public StoreService(StoreRepository repo) {
        this.repo = repo;
    }

    public List<Store> getAllStores() {
        return repo.findAll();
    }

    public Optional<Store> getStoreById(Long id) {
        return repo.findById(id);
    }

    public List<Store> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public List<Store> searchByLocation(String location) {
        return repo.findByLocationContainingIgnoreCase(location);
    }

    public List<Store> searchByEmail(String email) {
        return repo.findByEmailContainingIgnoreCase(email);
    }

    public List<Store> searchByPhone(String phone) {
        return repo.findByPhoneContaining(phone);
    }

    public List<Store> sortByName(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return repo.findAllByOrderByNameDesc();
        }
        return repo.findAllByOrderByNameAsc();
    }

    public List<Store> sortByLocation(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return repo.findAllByOrderByLocationDesc();
        }
        return repo.findAllByOrderByLocationAsc();
    }

    public Store saveStore(Store store) {
        return repo.save(store);
    }

    public void deleteStore(Long id) {
        repo.deleteById(id);
    }
}
