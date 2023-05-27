package com.sirdave.buildspace.payment

import com.sirdave.buildspace.transaction.Transaction

interface  PaymentService {

    fun charge(email: String,
               amount : Double,
               cardCvv: String,
               cardNumber: String,
               cardExpiryMonth: String,
               cardExpiryYear: String,
               pin: String,
               type: String
    ): Transaction

    fun retrievePaymentStatus(payload: String)
}