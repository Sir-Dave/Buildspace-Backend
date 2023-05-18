package com.sirdave.buildspace.helper

import com.sirdave.buildspace.constants.AuthorityConstants.MANAGE_INVENTORY
import com.sirdave.buildspace.constants.AuthorityConstants.PURCHASE_SUBSCRIPTION
import com.sirdave.buildspace.constants.AuthorityConstants.USER_CREATE
import com.sirdave.buildspace.constants.AuthorityConstants.USER_DELETE
import com.sirdave.buildspace.constants.AuthorityConstants.USER_READ
import com.sirdave.buildspace.constants.AuthorityConstants.USER_UPDATE


object Authority {
    val USER_AUTHORITIES = arrayOf(
        PURCHASE_SUBSCRIPTION
    )

    val ADMIN_AUTHORITIES = arrayOf(
        USER_CREATE,
        USER_READ,
        USER_UPDATE,
        USER_DELETE,
        MANAGE_INVENTORY
    )
}