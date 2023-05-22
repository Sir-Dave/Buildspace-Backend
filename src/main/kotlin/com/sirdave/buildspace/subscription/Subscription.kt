package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.helper.SubscriptionType
import com.sirdave.buildspace.user.User
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "subscriptions")
class Subscription(
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @Enumerated(EnumType.STRING)
    val type: SubscriptionType,

    val startDate: LocalDateTime,
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

    val endDate: LocalDateTime = startDate.plusDays(type.numberOfDays.toLong())

    @Transient
    val isExpired: Boolean = endDate.isBefore(LocalDateTime.now())

    fun addToUserSubscription(user: User) {
        user.addSubscription(this)
    }

    fun deleteSubscription(user: User){
        user.deleteSubscription(this)
    }
}