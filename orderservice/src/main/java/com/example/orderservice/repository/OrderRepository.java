package com.example.orderservice.repository;

import com.example.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStoreId(Long storeId);
    List<Order> findByProductId(Long productId);
    List<Order> findByOrderDateBetween(LocalDateTime from, LocalDateTime to);
    List<Order> findAllByOrderByOrderDateAsc();
    List<Order> findAllByOrderByOrderDateDesc();
    List<Order> findAllByOrderByQuantityDesc();
    List<Order> findAllByOrderByUnitPriceDesc();

    @Query("SELECT o.productId, SUM(o.quantity) as totalSold FROM Order o GROUP BY o.productId ORDER BY totalSold DESC")
    List<Object[]> getTopProducts();

    @Query("SELECT o.storeId, SUM(o.quantity) as totalSold FROM Order o GROUP BY o.storeId ORDER BY totalSold DESC")
    List<Object[]> getTopStores();

    @Query("SELECT DATE(o.orderDate), SUM(o.quantity) as totalSold FROM Order o GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> getSalesEvolution();
}
