package com.sirdave.buildspace.payment

import com.sirdave.buildspace.transaction.Transaction
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["api/v1/payments"])
class PaymentController(private val paymentService: PaymentService) {

    @Operation(summary = "Users make payment using this endpoint.")
    @PostMapping
    fun makePayment(@RequestParam email: String,
                    @RequestParam amount: Double,
                    @RequestParam cardCvv: String,
                    @RequestParam cardNumber: String,
                    @RequestParam cardExpiryMonth: String,
                    @RequestParam cardExpiryYear: String,
                    @RequestParam pin: String,
                    @RequestParam subscriptionType: String
    ): ResponseEntity<Transaction> {

        val response = paymentService.charge(
            email, amount, cardCvv, cardNumber,
            cardExpiryMonth, cardExpiryYear, pin,
            subscriptionType
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @Operation(summary = "Webhook to retrieve status of transaction")
    @PostMapping("/webhook")
    fun retrievePaymentStatus(@RequestBody payload: String) {
        paymentService.retrievePaymentStatus(payload)
    }
}