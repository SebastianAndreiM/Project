package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    public List<Order> getAllOrders() {
        return repo.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return repo.findById(id);
    }

    public List<Order> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public List<Order> getByStore(Long storeId) {
        return repo.findByStoreId(storeId);
    }

    public List<Order> getByProduct(Long productId) {
        return repo.findByProductId(productId);
    }

    public List<Order> getByPeriod(LocalDateTime from, LocalDateTime to) {
        return repo.findByOrderDateBetween(from, to);
    }

    public List<Order> sortByDate(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return repo.findAllByOrderByOrderDateDesc();
        }
        return repo.findAllByOrderByOrderDateAsc();
    }

    public List<Order> sortByQuantityDesc() {
        return repo.findAllByOrderByQuantityDesc();
    }

    public List<Order> sortByPriceDesc() {
        return repo.findAllByOrderByUnitPriceDesc();
    }

    public Order saveOrder(Order order) {
        return repo.save(order);
    }

    public void deleteOrder(Long id) {
        repo.deleteById(id);
    }

    // Statistici/rapoarte
    public List<Object[]> getTopProducts() {
        return repo.getTopProducts();
    }

    public List<Object[]> getTopStores() {
        return repo.getTopStores();
    }

    public List<Object[]> getSalesEvolution() {
        return repo.getSalesEvolution();
    }
}
