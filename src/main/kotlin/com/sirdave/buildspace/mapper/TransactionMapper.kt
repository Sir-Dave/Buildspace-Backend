package com.sirdave.buildspace.mapper

import com.sirdave.buildspace.transaction.Transaction
import com.sirdave.buildspace.transaction.TransactionDto
import java.time.format.DateTimeFormatter
import java.util.*

fun Transaction.toTransactionDto(): TransactionDto{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

    val dtoDate = date?.format(formatter)

    return TransactionDto(
        id = id.toString(),
        amount = amount ?: 0.0,
        reference = reference,
        date = dtoDate ?: "Undefined",
        status = status!!.name,
        userEmail = userEmail!!,
        subscriptionType =  subscriptionType!!,
        currency = currency
    )

}