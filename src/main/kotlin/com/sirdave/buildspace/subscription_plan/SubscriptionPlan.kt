package com.sirdave.buildspace.subscription_plan

import javax.persistence.*

@Entity
@Table(name = "subscription_plans")
data class SubscriptionPlan(
    var name: String,
    var amount: Double,
    var numberOfDays: Int,

    @Enumerated(value = EnumType.STRING)
    var type: SubscriptionType,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plan_sequence")
    @SequenceGenerator(name = "plan_sequence", sequenceName = "plan_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    val id: Int? = null,
)


enum class SubscriptionType{
    INDIVIDUAL,
    TEAM
}