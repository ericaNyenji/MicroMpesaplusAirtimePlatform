package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import org.springframework.stereotype.Service;

@Service
public class AirtimeService {

    private final WalletTransactionService walletTxService;

    public AirtimeService(WalletTransactionService walletTxService) {
        this.walletTxService = walletTxService;
    }

    public void buyAirtimeByPhone(String phone, double amount) {
        walletTxService.buyAirtime(phone, amount);
    }

    public double getAirtimeBalanceByPhone(String phone) {
        return walletTxService.getAirtimeBalance(phone);
    }
}











