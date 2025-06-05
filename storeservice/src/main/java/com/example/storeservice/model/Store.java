package org.example.storeservice.model;

import jakarta.persistence.*;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location; // Oraș/adresă
    private String phone;
    private String email;
    private String logoUrl; // (optional) Path/URL imagine logo

    public Store() {}

    public Store(String name, String location, String phone, String email, String logoUrl) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.logoUrl = logoUrl;
    }

    // Getteri/Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
}
