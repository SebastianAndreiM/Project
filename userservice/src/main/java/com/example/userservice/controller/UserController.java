package com.example.userservice.controller;

import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.export.UserExportFactory;
import com.example.userservice.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    // CRUD standard
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return service.saveUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return service.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }

    // Filtrare/căutare
    @GetMapping("/role/{role}")
    public List<User> getByRole(@PathVariable User.Role role) {
        return service.getByRole(role);
    }

    @GetMapping("/search/username/{username}")
    public List<User> searchByUsername(@PathVariable String username) {
        return service.searchByUsername(username);
    }

    @GetMapping("/search/email/{email}")
    public List<User> searchByEmail(@PathVariable String email) {
        return service.searchByEmail(email);
    }

    // LOGIN JWT (doar EMPLOYEE, MANAGER, ADMIN)
    @PostMapping("/login")
    public ResponseEntity<?> loginJwt(@RequestParam String username, @RequestParam String password) {
        Optional<User> user = service.login(username, password);
        if (user.isPresent()) {
            String jwt = JwtUtil.generateToken(user.get().getUsername(), user.get().getRole().name());
            // Trimitem și date despre user dacă vrei să le folosești pe frontend
            return ResponseEntity.ok().body(Map.of(
                    "jwt", jwt,
                    "username", user.get().getUsername(),
                    "role", user.get().getRole()
            ));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acces interzis (user inexistent sau rol nepotrivit)");
    }

    // EXEMPLU ENDPOINT PROTEJAT – cere header "Authorization: Bearer <token>"
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Claims claims = JwtUtil.validateToken(token);
            String username = claims.getSubject();
            String role = (String) claims.get("role");
            Optional<User> user = service.getByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "username", username,
                        "role", role,
                        "email", user.get().getEmail()
                ));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Userul nu există!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid sau expirat!");
        }
    }

    // Export CSV/JSON/XML/DOC
    @GetMapping("/export/{type}")
    public ResponseEntity<?> exportUsers(@PathVariable String type) throws IOException {
        ByteArrayInputStream stream = UserExportFactory.export(type, service.getAllUsers());
        String filename = "users." + type;
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
