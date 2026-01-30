package com.erica.fintech.MicroMpesaplusAirtimePlatform.repository;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhone(String phone);
}

