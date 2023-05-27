package com.sirdave.buildspace.transaction

import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.subscription.Subscription
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "transactions")
class Transaction (
    val amount: Double,
    val reference: String,
    val date: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var status: Status,

    val currency: String = "NGN"){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    val id: Long? = null

    @OneToOne
    var subscription: Subscription? = null
}