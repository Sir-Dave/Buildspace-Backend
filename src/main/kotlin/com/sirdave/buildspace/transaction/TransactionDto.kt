package com.sirdave.buildspace.transaction

data class TransactionDto(
    val id: String,
    val amount: Double,
    val reference: String,
    val date: String,
    val status: String,
    val userEmail: String,
    val subscriptionType: String,
    val currency: String
)