package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.event.PaymentSuccessEvent
import com.sirdave.buildspace.subscription.SubscriptionService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PaymentListener(private val subscriptionService: SubscriptionService) {

    @EventListener
    fun onPaymentEvent(event: PaymentSuccessEvent){
        subscriptionService.createSubscription(
            event.transaction.userEmail,
            event.transaction.subscriptionType
        )
    }
}