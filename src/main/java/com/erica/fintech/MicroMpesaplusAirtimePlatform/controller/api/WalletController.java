package com.erica.fintech.MicroMpesaplusAirtimePlatform.controller.api;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.dto.DepositRequest;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.service.DepositService;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final DepositService depositService;

    public WalletController(WalletService walletService, DepositService depositService) {
        this.walletService = walletService;
        this.depositService = depositService;
    }

    @GetMapping("/balance")
    public double getBalance(@AuthenticationPrincipal UserDetails userDetails) throws Exception {

        String phone = userDetails.getUsername();
        return walletService.getBalanceByPhone(phone);
    }


    @PostMapping("/deposit")
    public String deposit(@AuthenticationPrincipal UserDetails userDetails,
                          @Valid @RequestBody DepositRequest request) {

        depositService.deposit(userDetails.getUsername(), request.amount());
        return "Deposit successful";
    }
}