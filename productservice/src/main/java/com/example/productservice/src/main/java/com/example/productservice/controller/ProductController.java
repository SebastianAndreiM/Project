package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import com.example.productservice.export.ProductExportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    // CRUD standard
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return service.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
    }

    // Filtrare/Căutare
    @GetMapping("/search/model/{model}")
    public List<Product> searchByModel(@PathVariable String model) {
        return service.searchByModel(model);
    }

    @GetMapping("/search/producer/{producer}")
    public List<Product> searchByProducer(@PathVariable String producer) {
        return service.searchByProducer(producer);
    }

    @GetMapping("/search/category/{category}")
    public List<Product> searchByCategory(@PathVariable String category) {
        return service.searchByCategory(category);
    }

    @GetMapping("/filter/price")
    public List<Product> filterByPrice(@RequestParam double min, @RequestParam double max) {
        return service.filterByPrice(min, max);
    }

    // Sortare
    @GetMapping("/sort/price")
    public List<Product> sortByPrice(@RequestParam(defaultValue = "asc") String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return service.sortByPriceDesc();
        }
        return service.sortByPriceAsc();
    }

    // Export CSV/JSON/XML/DOC
    @GetMapping("/export/{type}")
    public ResponseEntity<?> exportProducts(@PathVariable String type) throws IOException {
        ByteArrayInputStream stream = ProductExportFactory.export(type, service.getAllProducts());
        String filename = "products." + type;
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

    // UPLOAD imagine produs
    @PostMapping("/{id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = "uploads/products/";
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileName = id + "_" + originalFilename;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // Updatează imageUrl în produs
            Optional<Product> opt = service.getProductById(id);
            if (opt.isPresent()) {
                Product p = opt.get();
                p.setImageUrl(uploadDir + fileName);
                service.saveProduct(p);
            }
            return ResponseEntity.ok(uploadDir + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la upload: " + e.getMessage());
        }
    }

    // DOWNLOAD imagine produs
    @GetMapping("/{id}/image")
    public ResponseEntity<?> downloadImage(@PathVariable Long id) throws IOException {
        Optional<Product> opt = service.getProductById(id);
        if (opt.isPresent() && opt.get().getImageUrl() != null) {
            String imgPath = opt.get().getImageUrl();
            File imgFile = new File(imgPath);
            if (!imgFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imaginea nu există!");
            }
            InputStreamResource resource = new InputStreamResource(new FileInputStream(imgFile));
            // Ghicește tipul MIME în funcție de extensie
            MediaType mediaType = imgPath.toLowerCase().endsWith(".png") ?
                    MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produsul nu are imagine încărcată!");
    }
}
