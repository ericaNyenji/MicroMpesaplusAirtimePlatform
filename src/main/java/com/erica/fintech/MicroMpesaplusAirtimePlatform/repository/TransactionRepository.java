package com.erica.fintech.MicroMpesaplusAirtimePlatform.repository;

import com.erica.fintech.MicroMpesaplusAirtimePlatform.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByOwnerIdOrderByTimestampDesc(Integer ownerId);//Fix repository query

    @Query("""
        SELECT COALESCE(SUM(t.amount), 0)
        FROM Transaction t
        WHERE t.ownerId = :userId
          AND t.receiverId IS NULL
          AND t.note = 'Airtime purchase'
    """) double sumAirtimeByUserId(@Param("userId") Integer userId);

}
