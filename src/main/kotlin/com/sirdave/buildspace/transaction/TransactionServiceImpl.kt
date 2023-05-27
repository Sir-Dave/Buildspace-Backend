package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.Status
import org.springframework.stereotype.Service
import java.util.*
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
    override fun updateTransactionStatus(id: Long, status: String): Transaction {
        val transaction = findTransactionById(id)
        transaction.status = getStatus(status)
        return transaction
    }

    override fun saveTransaction(transaction: Transaction): Transaction {
        return repository.save(transaction)    //TODO: Might have to do checks here later, using references
    }

    private fun isValidStatus(status: String): Boolean{
        return Arrays.stream(Status.values()).anyMatch {
            it.name.equals(status, ignoreCase = true)
        }
    }

    private fun getStatus(status: String): Status{
        check(isValidStatus(status)){
            throw IllegalStateException("Invalid status")
        }
        return Status.valueOf(status)
    }
}