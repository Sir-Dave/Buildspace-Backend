package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.getSubscriptionType
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

    override fun createSubscription(userEmail: String, type: String) {
        val user = userService.findUserByEmail(userEmail)
        val subscription = Subscription(
            user = user,
            type = getSubscriptionType(type)
        )

        subscription.addToUserSubscription(user)
        user.renewSubscription(subscription)
        repository.save(subscription)
    }
}