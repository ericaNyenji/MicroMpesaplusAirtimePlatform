package com.erica.fintech.MicroMpesaplusAirtimePlatform.service;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Transaction;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.TransactionRepository;
import com.erica.fintech.MicroMpesaplusAirtimePlatform.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository txRepo;
    private final UserRepository userRepo;

    public TransactionService(TransactionRepository txRepo, UserRepository userRepo) {
        this.txRepo = txRepo;
        this.userRepo = userRepo;
    }

    public List<Transaction> getUserTransactionsByPhone(String phone) {
        User user = userRepo.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return txRepo.findByOwnerIdOrderByTimestampDesc(user.getId());
    }

}

