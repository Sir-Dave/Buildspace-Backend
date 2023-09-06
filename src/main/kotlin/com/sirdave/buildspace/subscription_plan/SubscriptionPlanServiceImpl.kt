package com.sirdave.buildspace.subscription_plan

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.helper.getEnumName
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class SubscriptionPlanServiceImpl(
    private val repository: SubscriptionPlanRepository): SubscriptionPlanService {

    override fun findById(id: Int): SubscriptionPlan {
        return repository.findById(id)
            .orElseThrow { EntityNotFoundException("No plan with id $id exists") }
    }

    override fun findByName(name: String): SubscriptionPlan {
        return repository.findByName(name)
            .orElseThrow { EntityNotFoundException("No plan with name $name exists") }
    }


    override fun createPlan(name: String, amount: Double,
                            numberOfDays: Int, type: String): SubscriptionPlan {
        val planAlreadyExists = repository.findByName(name).isPresent
        if (planAlreadyExists){
            throw IllegalStateException("A plan with that name exists")
        }

        val subscriptionType = getEnumName<SubscriptionType>(type)
        val plan = SubscriptionPlan(name, amount, numberOfDays, subscriptionType)
        return repository.save(plan)
    }

    @Transactional
    override fun updatePlan(id: Int, name: String, amount: Double,
                            numberOfDays: Int, type: String): SubscriptionPlan {

        val plan = findById(id)
        val subscriptionType = getEnumName<SubscriptionType>(type)
        plan.apply {
            if (name.isNotBlank())
                plan.name = name

            if (amount > 0.0)
                plan.amount = amount

            if (numberOfDays > 0)
                plan.numberOfDays = numberOfDays

            plan.type = subscriptionType
        }
        return plan
    }

    override fun getPlanByType(type: String): List<SubscriptionPlan> {
        return repository.getPlanByType(type)
    }

}