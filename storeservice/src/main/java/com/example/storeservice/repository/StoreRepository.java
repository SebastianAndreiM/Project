package org.example.storeservice.repository;

import org.example.storeservice.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Pattern: Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByNameContainingIgnoreCase(String name);
    List<Store> findByLocationContainingIgnoreCase(String location);
    List<Store> findByEmailContainingIgnoreCase(String email);
    List<Store> findByPhoneContaining(String phone);

    List<Store> findAllByOrderByNameAsc();
    List<Store> findAllByOrderByNameDesc();
    List<Store> findAllByOrderByLocationAsc();
    List<Store> findAllByOrderByLocationDesc();
}
