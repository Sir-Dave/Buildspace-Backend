package com.sirdave.buildspace.auth

interface AuthService {
    fun register(registerRequest: RegisterRequest)
}