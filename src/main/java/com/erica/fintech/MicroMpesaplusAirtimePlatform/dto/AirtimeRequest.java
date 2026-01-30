package com.erica.fintech.MicroMpesaplusAirtimePlatform.dto;

import jakarta.validation.constraints.Positive;//This annotation comes from Jakarta Bean Validation. Spring Boot does NOT include validation by default unless you explicitly add it.


public record AirtimeRequest( @Positive(message = "Airtime amount must be greater than zero")double amount )   {

    }

