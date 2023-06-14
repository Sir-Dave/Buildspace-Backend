package com.sirdave.buildspace.payment

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName


data class ChargeRequest(
    val email: String,
    val amount : String,
    val card: Card,
    val pin: String
)

data class OTPRequest(
    val otp: String,
    val reference: String
)

data class Card(
    val cvv: String,

    val number: String,

    @SerializedName("expiry_month")
    val expiryMonth: String,

    @SerializedName("expiry_year")
    val expiryYear: String
)

data class TransactionResponse(
    val status: Boolean?,
    val message: String?,
    val event: String?,
    val data: Data
)

data class Data(
    val amount: Double?,
    val currency: String?,

    @JsonProperty("transaction_date")
    val transactionDate: String?,

    val status: String?,
    val reference: String?,
    val domain: String?,

    @JsonProperty("gateway_response")
    val gatewayResponse: String?,

    @JsonProperty("paid_at")
    val paidAt: String?,

    @JsonProperty("created_at")
    val createdAt: String?,

    val fees: Double,

    val message: String?,
    val channel: String?,
    val authorization: Authorization?,
    val customer: Customer?,
)

data class Authorization(
    @JsonProperty("authorization_code")
    val authorizationCode: String?,

    val bin: String?,
    val last4: String?,

    @JsonProperty("exp_month")
    val expiryMonth: String?,

    @JsonProperty("exp_year")
    val expiryYear: String?,

    val channel: String?,

    @JsonProperty("card_type")
    val cardType: String?,

    val bank: String?,

    @JsonProperty("country_code")
    val countryCode:String?,

    val brand: String?,
    val reusable: Boolean?,
    val signature: String?,
)

data class Customer(
    val id: Int?,

    @JsonProperty("first_name")
    val firstName: String?,

    @JsonProperty("last_name")
    val lastName: String?,

    val email: String?,

    @JsonProperty("customer_code")
    val customerCode: String?,

    @JsonProperty("risk_action")
    val riskAction: String?,
)