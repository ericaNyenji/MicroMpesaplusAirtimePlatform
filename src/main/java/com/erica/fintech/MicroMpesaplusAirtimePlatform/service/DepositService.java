package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import org.springframework.stereotype.Service;

@Service
public class DepositService {

    private final WalletTransactionService walletTxService;

    public DepositService(WalletTransactionService walletTxService) {
        this.walletTxService = walletTxService;
    }

    public void deposit(String phone, double amount) {
        walletTxService.deposit(phone, amount);
    }

}

