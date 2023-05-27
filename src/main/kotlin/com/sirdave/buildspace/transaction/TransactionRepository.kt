package com.sirdave.buildspace.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {

    fun findTransactionByReference(reference: String): Optional<Transaction>
}