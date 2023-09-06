package com.sirdave.buildspace.payment

import com.google.gson.Gson
import com.sirdave.buildspace.event.PaymentSuccessEvent
import com.sirdave.buildspace.exception.PaymentException
import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.helper.formatDate
import com.sirdave.buildspace.transaction.Transaction
import com.sirdave.buildspace.transaction.TransactionService
import com.sirdave.buildspace.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Service
class PaymentServiceImpl(
    private val userService: UserService,
    private val transactionService: TransactionService,
    private val publisher: ApplicationEventPublisher
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
        type: String,
        numDays: Int
    ): ResponseEntity<TransactionResponse> {

        val user = userService.findUserByEmail(email)
        if (user.currentSubscription != null)
            throw IllegalStateException("Current subscription is still active for user")

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


        val transaction = Transaction(
            amount = response.data.amount,
            reference = response.data.reference ?: "",
            date = if (response.data.paidAt == null) null
            else formatDate(response.data.paidAt),
            status = Status.PENDING,
            userEmail = email,
            subscriptionType =  type,
            numDays = numDays,
            currency = response.data.currency ?: "NGN"
        )

        transactionService.saveTransaction(transaction)

        if (response.data.status == "send_otp"){
            return ResponseEntity(response, HttpStatus.ACCEPTED)
        }

        return ResponseEntity(response, HttpStatus.CREATED)
    }

    override fun sendOTP(otp: String, reference: String): TransactionResponse {
        val otpRequest = OTPRequest(otp, reference)

        val jsonRequest = gson.toJson(otpRequest)

        val request = HttpRequest.newBuilder()
            .uri(URI("${BASE_URL}/charge/submit_otp"))
            .header("Authorization", "Bearer $secretKey")
            .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
            .build()


        val httpClient = HttpClient.newHttpClient()
        val httpResponse: HttpResponse<String>
        try {
            httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        }
        catch (exception: Exception){
            logger.error("Submit OTP Error: ${exception.message}")
            throw IOException("An error occurred while processing your request")
        }

        if (httpResponse.statusCode() !in listOf(200, 201)){
            logger.error("Submit OTP Error: ${httpResponse.body()}")
            throw PaymentException("An error occurred while charging your card")
        }

        val response = gson.fromJson(httpResponse.body(), TransactionResponse::class.java)
        logger.info("Submit OTP Request: response is $response")

        return response
    }

    override fun retrievePaymentStatus(payload: String) {
        val parsedPayload = gson.fromJson(payload, TransactionResponse::class.java)
        logger.info("parsedPayload is $parsedPayload")
        val event = parsedPayload.event

        if (event == "charge.success"){
            val transaction = transactionService.findTransactionByReference(
                parsedPayload.data.reference!!)
            publisher.publishEvent(PaymentSuccessEvent(transaction, parsedPayload))
        }
    }
}