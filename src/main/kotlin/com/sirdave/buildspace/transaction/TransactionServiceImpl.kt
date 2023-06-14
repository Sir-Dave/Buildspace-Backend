package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.Status
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class TransactionServiceImpl(private val repository: TransactionRepository): TransactionService {

    override fun findTransactionById(id: Long): Transaction {
        return repository.findById(id)
            .orElseThrow { EntityNotFoundException("Transaction not found") }
    }

    override fun findTransactionByReference(reference: String): Transaction {
        return repository.findTransactionByReference(reference)
            .orElseThrow { EntityNotFoundException("Transaction not found") }
    }

    @Transactional
    override fun updateTransaction(
        reference: String,
        amount: Double?,
        date: LocalDateTime?,
        status: Status,
        currency: String
    ): Transaction {
        val transaction = findTransactionByReference(reference)
        transaction.apply {
            this.amount = amount
            this.date = date
            this.status = status
            this.currency = currency
        }
        return transaction
    }

    override fun saveTransaction(transaction: Transaction): Transaction {
        return repository.save(transaction)
    }
}