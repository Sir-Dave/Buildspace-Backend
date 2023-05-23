package com.sirdave.buildspace.user

import com.sirdave.buildspace.subscription.Subscription
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "customers")
class User (
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String?,
    var password: String,
    var dateJoined: LocalDateTime,
    var role: String,
    var authorities: Array<String>,
    var isActive: Boolean,
    var isNotLocked: Boolean){

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

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val subscriptions: MutableSet<Subscription> = mutableSetOf()

    @OneToOne
    var currentSubscription: Subscription? = null

    fun addSubscription(subscription: Subscription) {
        subscriptions.add(subscription)
        subscription.user = this
    }

    fun deleteSubscription(subscription: Subscription){
        subscriptions.remove(subscription)
    }

    fun renewSubscription(subscription: Subscription){
        currentSubscription = subscription
    }

    fun cancelSubscription(){
        currentSubscription = null
    }


}