package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.event.PaymentSuccessEvent
import com.sirdave.buildspace.helper.Status
import com.sirdave.buildspace.helper.formatDate
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
        var transaction = event.transaction
        val response = event.payload

        if (transaction.status == Status.PENDING){

            transaction = transactionService.updateTransaction(
                transaction,
                amount = response.data.amount,
                date = if (response.data.paidAt == null) null else formatDate(response.data.paidAt),
                status = Status.COMPLETED,
                currency = response.data.currency ?: "NGN"
            )

            subscriptionService.createSubscription(
                transaction.userEmail!!,
                transaction.subscriptionPlan!!
            )
        }
    }
}