package com.sirdave.buildspace.token

interface TokenService {

    fun findByToken(token: String): Token

    fun confirmToken(token: String): Token

    fun saveToken(token: Token)
}