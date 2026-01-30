package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Notification;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.NotificationRepository;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final NotificationSocketService socket;
    private final UserRepository userRepo;

    public NotificationService(NotificationRepository repo,UserRepository userRepo,
                               NotificationSocketService socket) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.socket = socket;
    }

    public void notify(User user, String message, double newBalance) {

        Notification n = new Notification();
        n.setUserId(user.getId());
        n.setMessage(message);
        repo.save(n);

        Map<String, Object> payload = Map.of(
                "message", message,
                "balance", newBalance
        );

        socket.pushToUser(user.getPhone(), payload);
    }

    private User getUser(UserDetails userDetails)
    {
        return userRepo.findByPhone(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public List<Notification> getMyNotifications(UserDetails userDetails) {
        User user = getUser(userDetails);
        return repo.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public long getUnreadCount(UserDetails userDetails) {
        User user = getUser(userDetails);
        return repo.countByUserIdAndIsReadFalse(user.getId());
    }
}

