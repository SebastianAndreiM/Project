package com.example.notificationservice.service;

import com.example.notificationservice.model.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(Notification notification) {
        // Simulare trimitere email/SMS/WhatsApp
        System.out.println("NOTIFICARE către " + notification.getTo() + ": " + notification.getMessage());
        // În realitate, aici s-ar face apel la un serviciu extern (Mailgun, Twilio etc.)
    }
}
