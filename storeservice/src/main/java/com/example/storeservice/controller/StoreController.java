package org.example.storeservice.controller;

import org.example.storeservice.model.Store;
import org.example.storeservice.service.StoreService;
import org.example.storeservice.export.StoreExportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/stores")
public class StoreController {
    private final StoreService service;

    @Autowired
    public StoreController(StoreService service) {
        this.service = service;
    }

    // CRUD standard
    @GetMapping
    public List<Store> getAllStores() {
        return service.getAllStores();
    }

    @GetMapping("/{id}")
    public Optional<Store> getStoreById(@PathVariable Long id) {
        return service.getStoreById(id);
    }

    @PostMapping
    public Store addStore(@RequestBody Store store) {
        return service.saveStore(store);
    }

    @PutMapping("/{id}")
    public Store updateStore(@PathVariable Long id, @RequestBody Store store) {
        store.setId(id);
        return service.saveStore(store);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id) {
        service.deleteStore(id);
    }

    // Filtrare/Căutare
    @GetMapping("/search/name/{name}")
    public List<Store> searchByName(@PathVariable String name) {
        return service.searchByName(name);
    }

    @GetMapping("/search/location/{location}")
    public List<Store> searchByLocation(@PathVariable String location) {
        return service.searchByLocation(location);
    }

    @GetMapping("/search/email/{email}")
    public List<Store> searchByEmail(@PathVariable String email) {
        return service.searchByEmail(email);
    }

    @GetMapping("/search/phone/{phone}")
    public List<Store> searchByPhone(@PathVariable String phone) {
        return service.searchByPhone(phone);
    }

    // Sortare
    @GetMapping("/sort/name")
    public List<Store> sortByName(@RequestParam(defaultValue = "asc") String order) {
        return service.sortByName(order);
    }

    @GetMapping("/sort/location")
    public List<Store> sortByLocation(@RequestParam(defaultValue = "asc") String order) {
        return service.sortByLocation(order);
    }

    // Export CSV/JSON/XML/DOC
    @GetMapping("/export/{type}")
    public ResponseEntity<?> exportStores(@PathVariable String type) throws IOException {
        ByteArrayInputStream stream = StoreExportFactory.export(type, service.getAllStores());
        String filename = "stores." + type;
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

    // Upload logo (opțional)
    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        String dest = "uploads/logos/" + file.getOriginalFilename();
        File f = new File(dest);
        f.getParentFile().mkdirs();
        file.transferTo(f);
        Optional<Store> opt = service.getStoreById(id);
        if (opt.isPresent()) {
            Store s = opt.get();
            s.setLogoUrl(dest);
            service.saveStore(s);
        }
        return ResponseEntity.ok(dest);
    }
}
