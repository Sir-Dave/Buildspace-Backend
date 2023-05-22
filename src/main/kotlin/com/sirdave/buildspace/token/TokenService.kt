package com.sirdave.buildspace.token

import com.sirdave.buildspace.user.User

interface TokenService {

    fun findByToken(token: String): Token

    fun createVerificationToken(user: User): Token

    fun confirmToken(token: String)
}