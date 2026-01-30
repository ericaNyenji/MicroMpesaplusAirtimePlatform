package com.erica.fintech.MicroMpesaplusAirtimePlatform.repository;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Optional<Wallet> findByUserId(Integer userId);
}
