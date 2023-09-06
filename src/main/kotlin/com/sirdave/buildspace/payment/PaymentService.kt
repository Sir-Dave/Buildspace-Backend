package com.sirdave.buildspace.payment

import org.springframework.http.ResponseEntity

interface  PaymentService {

    fun charge(email: String,
               amount : Double,
               cardCvv: String,
               cardNumber: String,
               cardExpiryMonth: String,
               cardExpiryYear: String,
               pin: String,
               planName: String,
    ): ResponseEntity<TransactionResponse>

    fun sendOTP(otp: String, reference: String): TransactionResponse

    fun retrievePaymentStatus(payload: String)
}