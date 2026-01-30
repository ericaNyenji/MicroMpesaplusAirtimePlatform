package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/wallet")
    public String wallet() {
        return "wallet";
    }

    @GetMapping("/send-money")
    public String sendMoney() { return "sendMoney"; }

    @GetMapping("/airtime")
    public String airtime() { return "airtime"; }

    @GetMapping("/transactions-history")
    public String transactionsHistoryPage() { return "transactionsHistory"; }

    @GetMapping("/notifications")
    public String notificationsPage() { return "notifications"; }

}

