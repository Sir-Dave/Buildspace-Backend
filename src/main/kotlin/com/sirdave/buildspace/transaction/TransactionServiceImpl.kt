package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.mapper.toTransactionDto
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

    override fun getUserTransactions(email: String): Set<TransactionDto> {
        return repository.findAllTransactionsByEmail(email).map {
            it.toTransactionDto()
        }.toSet()
    }

    @Transactional
    override fun updateTransaction(
        transaction: Transaction,
        amount: Double?,
        date: LocalDateTime?,
        status: Status,
        currency: String
    ): Transaction {
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