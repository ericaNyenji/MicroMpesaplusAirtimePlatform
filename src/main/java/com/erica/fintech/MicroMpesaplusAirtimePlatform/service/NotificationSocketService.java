package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void pushToUser(String username, Object payload) {
        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                payload
        );
    }
}
