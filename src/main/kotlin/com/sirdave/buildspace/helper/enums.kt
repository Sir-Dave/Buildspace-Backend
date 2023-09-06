package com.sirdave.buildspace.helper

import com.sirdave.buildspace.helper.Authority.USER_AUTHORITIES
import com.sirdave.buildspace.helper.Authority.ADMIN_AUTHORITIES

enum class Role(val authorities: Array<String>) {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES)
}

enum class Status {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED
}