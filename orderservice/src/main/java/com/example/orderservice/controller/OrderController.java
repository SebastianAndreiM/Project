package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.export.OrderExportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        // 1. Orchestrare: scădere stoc din InventoryService
        RestTemplate rest = new RestTemplate();
        String inventoryUrl = String.format(
                "http://localhost:8083/inventories/decrement?storeId=%d&productId=%d&size=%s&color=%s&qty=%d",
                order.getStoreId(), order.getProductId(), order.getSize(), order.getColor(), order.getQuantity()
        );
        // Pentru %s (size, color), encode dacă pot avea spații!
        inventoryUrl = inventoryUrl.replace(" ", "%20");
        try {
            ResponseEntity<?> resp = rest.exchange(inventoryUrl, HttpMethod.PUT, null, Object.class);
            if (!resp.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu se poate plasa comanda: " + resp.getBody());
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare scădere stoc: " + ex.getMessage());
        }
        // 2. Salvează comanda
        Order saved = service.saveOrder(order);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return service.saveOrder(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
    }

    // Căutare/filtrare
    @GetMapping("/user/{userId}")
    public List<Order> getByUser(@PathVariable Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/store/{storeId}")
    public List<Order> getByStore(@PathVariable Long storeId) {
        return service.getByStore(storeId);
    }

    @GetMapping("/product/{productId}")
    public List<Order> getByProduct(@PathVariable Long productId) {
        return service.getByProduct(productId);
    }

    @GetMapping("/period")
    public List<Order> getByPeriod(@RequestParam String from, @RequestParam String to) {
        return service.getByPeriod(LocalDateTime.parse(from), LocalDateTime.parse(to));
    }

    // Sortare
    @GetMapping("/sort/date")
    public List<Order> sortByDate(@RequestParam(defaultValue = "asc") String order) {
        return service.sortByDate(order);
    }

    @GetMapping("/sort/quantity")
    public List<Order> sortByQuantityDesc() {
        return service.sortByQuantityDesc();
    }

    @GetMapping("/sort/price")
    public List<Order> sortByPriceDesc() {
        return service.sortByPriceDesc();
    }

    // Statistici/rapoarte pentru grafice
    @GetMapping("/stats/top-products")
    public List<Map<String, Object>> topProducts() {
        List<Object[]> results = service.getTopProducts();
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", row[0]);
            map.put("totalSold", row[1]);
            out.add(map);
        }
        return out;
    }

    @GetMapping("/stats/top-stores")
    public List<Map<String, Object>> topStores() {
        List<Object[]> results = service.getTopStores();
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("storeId", row[0]);
            map.put("totalSold", row[1]);
            out.add(map);
        }
        return out;
    }

    @GetMapping("/stats/sales-evolution")
    public List<Map<String, Object>> salesEvolution() {
        List<Object[]> results = service.getSalesEvolution();
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("totalSold", row[1]);
            out.add(map);
        }
        return out;
    }

    // Export CSV/JSON/XML/DOC
    @GetMapping("/export/{type}")
    public ResponseEntity<?> exportOrders(@PathVariable String type) throws IOException {
        ByteArrayInputStream stream = OrderExportFactory.export(type, service.getAllOrders());
        String filename = "orders." + type;
        MediaType mediaType = switch (type) {
            case "csv" -> MediaType.TEXT_PLAIN;
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            case "doc" -> MediaType.APPLICATION_OCTET_STREAM;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(mediaType)
                .body(new InputStreamResource(stream));
    }
}
