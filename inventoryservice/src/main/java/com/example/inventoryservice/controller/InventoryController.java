package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import com.example.inventoryservice.export.InventoryExportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/inventories")
public class InventoryController {
    private final InventoryService service;

    @Autowired
    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // CRUD standard
    @GetMapping
    public List<Inventory> getAllInventories() {
        return service.getAllInventories();
    }

    @GetMapping("/{id}")
    public Optional<Inventory> getInventoryById(@PathVariable Long id) {
        return service.getInventoryById(id);
    }

    @PostMapping
    public Inventory addInventory(@RequestBody Inventory inventory) {
        return service.saveInventory(inventory);
    }

    @PutMapping("/{id}")
    public Inventory updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        inventory.setId(id);
        return service.saveInventory(inventory);
    }

    @DeleteMapping("/{id}")
    public void deleteInventory(@PathVariable Long id) {
        service.deleteInventory(id);
    }

    // Filtrare/căutare
    @GetMapping("/store/{storeId}")
    public List<Inventory> getByStore(@PathVariable Long storeId) {
        return service.getByStore(storeId);
    }

    @GetMapping("/product/{productId}")
    public List<Inventory> getByProduct(@PathVariable Long productId) {
        return service.getByProduct(productId);
    }

    @GetMapping("/size/{size}")
    public List<Inventory> getBySize(@PathVariable String size) {
        return service.getBySize(size);
    }

    @GetMapping("/color/{color}")
    public List<Inventory> getByColor(@PathVariable String color) {
        return service.getByColor(color);
    }

    @GetMapping("/lowstock/{stock}")
    public List<Inventory> getByLowStock(@PathVariable int stock) {
        return service.getByLowStock(stock);
    }

    @GetMapping("/store/{storeId}/product/{productId}")
    public List<Inventory> getByStoreAndProduct(@PathVariable Long storeId, @PathVariable Long productId) {
        return service.getByStoreAndProduct(storeId, productId);
    }

    // Sortare
    @GetMapping("/sort/stock")
    public List<Inventory> sortByStock(@RequestParam(defaultValue = "asc") String order) {
        return service.sortByStock(order);
    }

    @GetMapping("/sort/size")
    public List<Inventory> sortBySize() {
        return service.sortBySize();
    }

    @GetMapping("/sort/color")
    public List<Inventory> sortByColor() {
        return service.sortByColor();
    }

    // Export CSV/JSON/XML/DOC
    @GetMapping("/export/{type}")
    public ResponseEntity<?> exportInventories(@PathVariable String type) throws IOException {
        ByteArrayInputStream stream = InventoryExportFactory.export(type, service.getAllInventories());
        String filename = "inventories." + type;
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
    @PutMapping("/decrement")
    public ResponseEntity<?> decrementStock(@RequestParam Long storeId,
                                            @RequestParam Long productId,
                                            @RequestParam String size,
                                            @RequestParam String color,
                                            @RequestParam int qty) {
        List<Inventory> found = service.getByStoreAndProduct(storeId, productId)
                .stream()
                .filter(i -> i.getSize().equals(size) && i.getColor().equals(color))
                .toList();

        if (found.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nu există stoc pentru această combinație!");
        }
        Inventory inv = found.get(0);
        if (inv.getStock() < qty) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stoc insuficient!");
        }
        inv.setStock(inv.getStock() - qty);
        service.saveInventory(inv);
        return ResponseEntity.ok(inv);
    }

}
