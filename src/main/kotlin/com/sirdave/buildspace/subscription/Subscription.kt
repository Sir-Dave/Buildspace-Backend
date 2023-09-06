package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.subscription_plan.SubscriptionPlan
import com.sirdave.buildspace.user.User
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "subscriptions")
class Subscription(
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @OneToOne
    @JoinColumn(name = "plan_id")
    val plan: SubscriptionPlan
){

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = [Parameter(
            name = "uuid_gen_strategy_class",
            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
        )]
    )
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID? = null


    val startDate: LocalDateTime = LocalDateTime.now()

    val endDate: LocalDateTime = startDate.plusDays(plan.numberOfDays.toLong())

    fun isExpired(): Boolean {
        return endDate.isBefore(LocalDateTime.now())
    }

    fun addToUserSubscription(user: User) {
        user.addSubscription(this)
    }

    fun deleteSubscription(user: User){
        user.deleteSubscription(this)
    }
}