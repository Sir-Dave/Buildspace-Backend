package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.event.PaymentSuccessEvent
import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.subscription.SubscriptionService
import com.sirdave.buildspace.transaction.TransactionService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentListener(
    private val subscriptionService: SubscriptionService,
    private val transactionService: TransactionService
    ) {

    @EventListener
    fun onPaymentEvent(event: PaymentSuccessEvent){
        val transaction = event.transaction

        if (transaction.status == Status.PENDING){

            transactionService.updateTransactionStatus(
                transaction.id!!, Status.COMPLETED.name
            )

            subscriptionService.createSubscription(
                transaction.userEmail,
                transaction.subscriptionType
            )
        }

    }
}