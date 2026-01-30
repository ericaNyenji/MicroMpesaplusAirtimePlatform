package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.api;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Notification;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.service.NotificationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> myNotifications(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return notificationService.getMyNotifications(userDetails);
    }

    @GetMapping("/unread-count")
    public long unreadCount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return notificationService.getUnreadCount(userDetails);
    }
}

