package com.sirdave.buildspace.mapper

import com.sirdave.buildspace.subscription.Subscription
import com.sirdave.buildspace.subscription.SubscriptionDto
import com.sirdave.buildspace.subscription.SubscriptionPlanDto
import com.sirdave.buildspace.subscription_plan.SubscriptionPlan
import java.time.format.DateTimeFormatter
import java.util.*

fun Subscription.toSubscriptionDto(): SubscriptionDto {

    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

    val dtoStartDate = startDate.format(formatter)
    val dtoEndDate = endDate.format(formatter)

    return SubscriptionDto(
        id = id.toString(),
        startDate = dtoStartDate,
        endDate = dtoEndDate,
        type = type.name,
        amount = type.amount,
        isExpired = isExpired(),
        user = user.toUserDto()
    )
}

fun SubscriptionPlan.toSubscriptionPlanDto(): SubscriptionPlanDto{
    return SubscriptionPlanDto(
        name = name,
        amount = amount,
        numberOfDays = numberOfDays,
        type = type.name
    )
}