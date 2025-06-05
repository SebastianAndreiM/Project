package com.example.notificationservice.controller;

import com.example.notificationservice.model.Notification;
import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public void send(@RequestBody Notification notification) {
        service.sendNotification(notification);
    }
}
