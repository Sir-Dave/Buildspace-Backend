package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.mapper.toSubscriptionDto
import com.sirdave.buildspace.user.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class SubscriptionServiceImpl(private val repository: SubscriptionRepository): SubscriptionService {

    override fun findById(id: UUID): Subscription {
        return repository.findById(id)
            .orElseThrow { EntityNotFoundException("No subscription with id $id was found") }
    }

    override fun findAllByUser(user: User): Set<SubscriptionDto> {
        return repository.findAllByUser(user).map {
            it.toSubscriptionDto()
        }.toSet()
    }

    override fun getUserCurrentSubscription(user: User): SubscriptionDto {
        if (user.currentSubscription == null)
            throw EntityNotFoundException("User has no current subscription")

        return user.currentSubscription!!.toSubscriptionDto()
    }

    override fun createSubscription(subscription: Subscription) {
        repository.save(subscription)
    }


}