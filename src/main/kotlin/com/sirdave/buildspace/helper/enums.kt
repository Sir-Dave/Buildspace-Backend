package com.sirdave.buildspace.helper

import com.sirdave.buildspace.helper.Authority.USER_AUTHORITIES
import com.sirdave.buildspace.helper.Authority.ADMIN_AUTHORITIES

enum class Role(val authorities: Array<String>) {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES)
}

enum class SubscriptionType(val amount: Double, val numberOfDays: Int, val type: String = "individual"){
    DAILY(1500.0, 1),
    WEEKLY(5000.0, 7),
    MONTHLY(10000.0, 28),
    MONTHLY_TEAM(40000.0, 30, "team"),
    QUARTERLY_TEAM(114000.0, 90, "team"),
    BIANNUAL_TEAM(216000.0,180, "team"),
    YEARLY_TEAM(408000.0, 365, "team")
}

enum class Status {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}