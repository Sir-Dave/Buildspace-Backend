package com.sirdave.buildspace.transaction

interface TransactionService {

    fun findTransactionById(id: Long): Transaction

    fun findTransactionByReference(reference: String): Transaction

    fun updateTransactionStatus(id: Long, status: String): Transaction

    fun saveTransaction(transaction: Transaction): Transaction
}