package com.sirdave.buildspace.payment

interface  PaymentService {

    fun charge(email: String,
               amount : Double,
               cardCvv: String,
               cardNumber: String,
               cardExpiryMonth: String,
               cardExpiryYear: String,
               pin: String
    ): TransactionResponse

    fun retrievePaymentStatus(payload: String)
}