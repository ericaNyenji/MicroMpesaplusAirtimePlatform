package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import org.springframework.stereotype.Service;

@Service
public class SendMoneyService {

    private final WalletTransactionService walletTxService;

        public SendMoneyService(WalletTransactionService walletTxService) {
            this.walletTxService = walletTxService;
        }

        public void send(String senderPhone, String receiverPhone, double amount) {
            walletTxService.sendMoney(senderPhone, receiverPhone, amount);
        }
}
