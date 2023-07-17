package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.user.UserDto

data class SubscriptionDto (
    val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val amount: Double,
    val isExpired: Boolean,
    var user: UserDto
)

data class SubscriptionPlan(
    val name: String,
    val amount: Double,
    val numberOfDays: Int,
)