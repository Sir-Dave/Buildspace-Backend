package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.user.UserDto

data class SubscriptionDto (
    val id: String,
    val startDate: String,
    val endDate: String,
    val type: String,
    val isExpired: Boolean,
    var user: UserDto
)