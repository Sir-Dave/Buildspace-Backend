package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.user.User
import java.util.*

interface SubscriptionService {

    fun findById(id: UUID): Subscription

    fun findAllByUser(user: User): Set<SubscriptionDto>

    fun getUserCurrentSubscription(user: User): SubscriptionDto

    fun createSubscription(subscription: Subscription)
}