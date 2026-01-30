package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Wallet;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepo;
    private final UserRepository userRepo;

    public WalletService(WalletRepository walletRepo, UserRepository userRepo) {
        this.walletRepo = walletRepo;
        this.userRepo = userRepo;
    }


    public double getBalanceByPhone(String phone) {
        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return walletRepo.findByUserId(user.getId())
                .map(Wallet::getBalance)
                .orElse(0.0);
    }

}

