package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.SubscriptionType
import com.sirdave.buildspace.mapper.toSubscriptionDto
import com.sirdave.buildspace.user.UserService
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubscriptionServiceImpl(
    private val repository: SubscriptionRepository,
    private val userService: UserService): SubscriptionService {

    override fun findById(id: UUID): Subscription {
        return repository.findById(id)
            .orElseThrow { EntityNotFoundException("No subscription with id $id was found") }
    }

    override fun findAllByUser(userId: UUID): Set<SubscriptionDto> {
        val user = userService.findUserById(userId)
        return repository.findAllByUser(user).map {
            it.toSubscriptionDto()
        }.toSet()
    }

    override fun getUserCurrentSubscription(userId: UUID): SubscriptionDto {
        val user = userService.findUserById(userId)
        if (user.currentSubscription == null)
            throw EntityNotFoundException("User has no current subscription")

        return user.currentSubscription!!.toSubscriptionDto()
    }

    override fun createSubscription(subscriptionRequest: SubscriptionRequest) {
        val userId = UUID.fromString(subscriptionRequest.userId)
        val user = userService.findUserById(userId)
        val subscription = Subscription(
            user = user,
            type = getSubscriptionType(subscriptionRequest.type)
        )

        if (user.currentSubscription != null)
            throw IllegalStateException("Current subscription is still active for user")

        subscription.addToUserSubscription(user)
        user.renewSubscription(subscription)
        repository.save(subscription)
    }

    private fun getSubscriptionType(name: String): SubscriptionType {
        check(isValidSubscriptionType(name)){
            throw IllegalStateException("Invalid subscription type")
        }
        return SubscriptionType.valueOf(name.uppercase())
    }

    private fun isValidSubscriptionType(name: String): Boolean {
        return Arrays.stream(SubscriptionType.values()).anyMatch { type -> type.name.equals(name, ignoreCase = true) }
    }

}