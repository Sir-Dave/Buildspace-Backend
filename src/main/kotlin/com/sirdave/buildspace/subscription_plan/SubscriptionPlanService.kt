package com.sirdave.buildspace.subscription_plan

interface SubscriptionPlanService {

    fun findById(id: Int): SubscriptionPlan

    fun findByName(name: String): SubscriptionPlan

    fun createPlan(name: String,
                   amount: Double,
                   numberOfDays: Int,
                   type: String): SubscriptionPlan

    fun updatePlan(
        id: Int,
        name: String,
        amount: Double,
        numberOfDays: Int,
        type: String
    ): SubscriptionPlan

    fun getPlanByType(type: String): List<SubscriptionPlan>
}