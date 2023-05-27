package com.sirdave.buildspace.payment

import com.google.gson.Gson
import com.sirdave.buildspace.exception.PaymentException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Service
class PaymentServiceImpl: PaymentService {

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
        pin: String
    ): TransactionResponse {
        val card = Card(cardCvv, cardNumber, cardExpiryMonth, cardExpiryYear)
        val chargeRequest = ChargeRequest(email, (amount * 100).toString(), card, pin)

        val jsonRequest = gson.toJson(chargeRequest)

        val request = HttpRequest.newBuilder()
            .uri(URI("${BASE_URL}/charge"))
            .header("Authorization", "Bearer $secretKey")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build()

        val httpClient = HttpClient.newHttpClient()
        val response: HttpResponse<String>
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        }
        catch (exception: Exception){
            logger.error("Charge Error: ${exception.message}")
            throw IOException("An error occurred while processing your request")
        }

        if (response.statusCode() !in listOf(200, 201)){
            logger.error("Charge Error: ${response.body()}")
            throw PaymentException("An error occurred while charging your card")
        }

        val transaction = gson.fromJson(response.body(), TransactionResponse::class.java)
        logger.info("Charge API: response is $transaction")

        return transaction
    }

    override fun retrievePaymentStatus(payload: String) {
        val parsedPayload = gson.fromJson(payload, TransactionResponse::class.java)

        // TODO: Publish an event that process the parsed payload
        logger.info("parsedPayload is ${parsedPayload.event}")

    }
}