package com.erica.fintech.MicroMpesaplusAirtimePlatform.dto;

import jakarta.validation.constraints.Positive;

public record DepositRequest( @Positive(message = "Deposit amount must be greater than zero")double amount ) {

}

