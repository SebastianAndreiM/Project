package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service // Pattern: Service Layer
public class UserService {
    private final UserRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repo.findById(id);
    }

    public Optional<User> getByUsername(String username) {
        return repo.findByUsername(username);
    }

    public List<User> getByRole(User.Role role) {
        return repo.findByRole(role);
    }

    public List<User> searchByUsername(String username) {
        return repo.findByUsernameContainingIgnoreCase(username);
    }

    public List<User> searchByEmail(String email) {
        return repo.findByEmailContainingIgnoreCase(email);
    }

    public User saveUser(User user) {
        boolean isUpdate = user.getId() != null && repo.existsById(user.getId());
        User saved = repo.save(user);

        // Trimite notificare DOAR la update, nu la creare user nou
        if (isUpdate) {
            try {
                String notificationUrl = "http://localhost:8085/notifications"; // portul notificationservice!
                String msg = "Userul " + saved.getUsername() + " și-a modificat datele!";
                Notification notif = new Notification(msg, saved.getEmail());
                restTemplate.postForEntity(notificationUrl, notif, Void.class);
            } catch (Exception e) {
                System.out.println("Notificarea nu a putut fi trimisă: " + e.getMessage());
            }
        }
        return saved;
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    // Login logic (doar EMPLOYEE, MANAGER, ADMIN pot da login)
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = repo.findByUsername(username);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            if (u.getPassword().equals(password) && u.getRole() != User.Role.CLIENT) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    // Clasă statică Notification pentru request-ul către NotificationService
    public static class Notification {
        private String message;
        private String to;

        public Notification() {}
        public Notification(String message, String to) {
            this.message = message;
            this.to = to;
        }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
    }
}
