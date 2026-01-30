package com.erica.fintech.MicroMpesaplusAirtimePlatform.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    private Integer userId;

    private double balance;

    public Wallet() {}

    public Wallet(Integer userId, double balance) { this.userId = userId; this.balance = balance; }

    public Integer getUserId() { return userId; }

    public void setUserId(Integer userId) { this.userId = userId; }

    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }
}
