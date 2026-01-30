package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.api;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class FullnameController {

    private final UserRepository userRepository;

    public FullnameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/byPhone")
    public ResponseEntity<?> getUserByPhone(@RequestParam String phone) {
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();
        Map<String, String> result = Map.of("fullName", userOpt.get().getFullName());
        return ResponseEntity.ok(result);
    }
}
