package com.sirdave.buildspace.payment

import com.google.gson.Gson
import com.sirdave.buildspace.exception.PaymentException
import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.transaction.Transaction
import com.sirdave.buildspace.transaction.TransactionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class PaymentServiceImpl(
    private val transactionService: TransactionService
    ): PaymentService {

    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val gson by lazy { Gson() }

    @Value("\${paystack-secret-key}")
    private val secretKey: String? = null

    companion object{
        const val BASE_URL = "https://api.paystack.co"
    }

    override fun charge(
        email: String,
        amount: Double,
        cardCvv: String,
        cardNumber: String,
        cardExpiryMonth: String,
        cardExpiryYear: String,
        pin: String,
        subscriptionType: String
    ): Transaction {
        val card = Card(cardCvv, cardNumber, cardExpiryMonth, cardExpiryYear)
        val chargeRequest = ChargeRequest(email, (amount * 100).toString(), card, pin)

        val jsonRequest = gson.toJson(chargeRequest)

        val request = HttpRequest.newBuilder()
            .uri(URI("${BASE_URL}/charge"))
            .header("Authorization", "Bearer $secretKey")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build()

        val httpClient = HttpClient.newHttpClient()
        val httpResponse: HttpResponse<String>
        try {
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        }
        catch (exception: Exception){
            logger.error("Charge Error: ${exception.message}")
            throw IOException("An error occurred while processing your request")
        }

        if (httpResponse.statusCode() !in listOf(200, 201)){
            logger.error("Charge Error: ${httpResponse.body()}")
            throw PaymentException("An error occurred while charging your card")
        }

        val response = gson.fromJson(httpResponse.body(), TransactionResponse::class.java)
        logger.info("Charge API: response is $response")

        val dateToBeFormatted = response.data.paidAt ?: ""

        val transaction = Transaction(
            amount = response.data.amount ?: 0.0,
            reference = response.data.reference ?: "",
            date = formatDate(dateToBeFormatted),
            status = Status.PENDING,
            userEmail = email,
            subscriptionType = subscriptionType,
            currency = response.data.currency ?: ""
        )

        return transactionService.saveTransaction(transaction)
    }

    override fun retrievePaymentStatus(payload: String) {
        val parsedPayload = gson.fromJson(payload, TransactionResponse::class.java)
        logger.info("parsedPayload is $parsedPayload")
    }

    private fun formatDate(date: String): LocalDateTime {
        val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
        return LocalDateTime.parse(date, formatter)
    }
}