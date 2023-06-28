package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.mapper.toTransactionDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/transactions"])
class TransactionController(private val transactionService: TransactionService) {

    @Operation(summary = "Get all transactions by a user")
    @GetMapping
    fun getAllTransactionsByUser(@RequestParam email: String): ResponseEntity<Set<TransactionDto>>{
        val transactions = transactionService.getUserTransactions(email)
        return ResponseEntity(transactions, HttpStatus.OK)
    }

    @Operation(summary = "Get details of one transaction")
    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable("id") id: Long): ResponseEntity<TransactionDto>{
        val transaction = transactionService.findTransactionById(id).toTransactionDto()
        return ResponseEntity(transaction, HttpStatus.OK)
    }
}