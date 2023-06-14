package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.helper.Status
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction(var reference: String){
    var amount: Double? = null
    var date: LocalDateTime? = null

    @Enumerated(EnumType.STRING)
    var status: Status? = null

    var userEmail: String? = null
    var subscriptionType: String? = null

    var currency: String = "NGN"

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    val id: Long? = null


    constructor(amount: Double?,
                reference: String,
                date: LocalDateTime?,
                status: Status,
                userEmail: String,
                subscriptionType: String,
                currency: String): this(reference){

        this.amount = amount
        this.reference = reference
        this.date = date
        this.status = status
        this.userEmail = userEmail
        this.subscriptionType = subscriptionType
        this.currency = currency
    }
}