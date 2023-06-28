package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.helper.Status
import java.time.LocalDateTime

interface TransactionService {

    fun findTransactionById(id: Long): Transaction

    fun findTransactionByReference(reference: String): Transaction

    fun getUserTransactions(email: String): Set<TransactionDto>

    fun updateTransaction(transaction: Transaction,
                          amount: Double?,
                          date: LocalDateTime?,
                          status: Status,
                          currency: String): Transaction

    fun saveTransaction(transaction: Transaction): Transaction
}