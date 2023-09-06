package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.getEnumName
import com.sirdave.buildspace.helper.getSubscriptionType
import com.sirdave.buildspace.mapper.toSubscriptionDto
import com.sirdave.buildspace.mapper.toSubscriptionPlanDto
import com.sirdave.buildspace.subscription_plan.SubscriptionPlanService
import com.sirdave.buildspace.subscription_plan.SubscriptionType
import com.sirdave.buildspace.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class SubscriptionServiceImpl(
    private val repository: SubscriptionRepository,
    private val userService: UserService,
    private val subscriptionPlanService: SubscriptionPlanService
): SubscriptionService {

    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

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

    override fun getAllSubscriptionPlans(type: String): List<SubscriptionPlanDto> {
        val subscriptionType = getEnumName<SubscriptionType>(type)
        return subscriptionPlanService.getPlanByType(subscriptionType)
            .map { it.toSubscriptionPlanDto() }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    override fun removeExpiredSubscriptions() {
        logger.info("Cron job: Removing expired subscriptions")
        val users = userService.retrieveAllUsers()
        users.map { user ->
            if (user.currentSubscription != null && user.currentSubscription!!.isExpired()){
                user.removeExpiredSubscription()
            }
        }
    }

    @Transactional
    override fun setExpiredFields() {
        val users = userService.retrieveAllUsers()
        users.map { user ->
            if (user.currentSubscription != null){
                user.removeExpiredSubscription()
            }
        }
    }
}