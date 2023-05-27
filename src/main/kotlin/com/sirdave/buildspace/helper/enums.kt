package com.sirdave.buildspace.helper

import com.sirdave.buildspace.helper.Authority.USER_AUTHORITIES
import com.sirdave.buildspace.helper.Authority.ADMIN_AUTHORITIES

enum class Role(val authorities: Array<String>) {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES)
}

enum class SubscriptionType(val amount: Double, val numberOfDays: Int){
    DAILY(1000.0, 1),
    WEEKLY(2500.0, 7),
    MONTHLY(7000.0, 28)
}

enum class Status {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}