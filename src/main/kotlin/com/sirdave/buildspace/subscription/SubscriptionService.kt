package com.sirdave.buildspace.subscription

import java.util.*

interface SubscriptionService {

    fun findById(id: UUID): Subscription

    fun findAllByUser(userId: UUID): Set<SubscriptionDto>

    fun getUserCurrentSubscription(userId: UUID): SubscriptionDto

    fun createSubscription(userEmail: String, planName: String)

    fun getAllSubscriptionPlans(type: String): List<SubscriptionPlanDto>

    fun removeExpiredSubscriptions()

    fun setExpiredFields()
}