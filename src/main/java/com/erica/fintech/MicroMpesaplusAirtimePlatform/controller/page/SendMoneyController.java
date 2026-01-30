package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.page;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.service.SendMoneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sendMoney")
public class SendMoneyController {

    private final SendMoneyService sendMoneyService;


    public SendMoneyController(SendMoneyService sendMoneyService) {
        this.sendMoneyService = sendMoneyService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> sendMoney(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String receiverPhone,
            @RequestParam double amount) {

        String senderPhone = userDetails.getUsername();

        try {
            sendMoneyService.send(senderPhone, receiverPhone, amount);
            return ResponseEntity.ok("Money sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error");
        }
    }

}
