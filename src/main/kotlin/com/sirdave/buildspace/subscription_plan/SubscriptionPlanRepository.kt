package com.sirdave.buildspace.subscription_plan

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SubscriptionPlanRepository: JpaRepository<SubscriptionPlan, Int> {

    fun findByName(name: String): Optional<SubscriptionPlan>

    @Query("SELECT s FROM SubscriptionPlan s WHERE s.type.name = :type")
    fun getPlanByType(type: String): List<SubscriptionPlan>
}