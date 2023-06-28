package com.sirdave.buildspace.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepository: JpaRepository<Transaction, Long> {

    fun findTransactionByReference(reference: String): Optional<Transaction>

    @Query("SELECT t FROM Transaction t WHERE t.userEmail = ?1 ORDER BY t.date DESC")
    fun findAllTransactionsByEmail(email: String): Set<Transaction>
}