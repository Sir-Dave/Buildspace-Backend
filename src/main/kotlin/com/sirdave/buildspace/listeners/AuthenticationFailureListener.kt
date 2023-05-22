package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.auth.LoginAttemptService
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Component

@Component
class AuthenticationFailureListener(private val loginAttemptService: LoginAttemptService) {

    @EventListener
    fun onAuthenticationFailure(event: AuthenticationFailureBadCredentialsEvent) {
        val principal = event.authentication.principal
        if (principal is String) {
            loginAttemptService.addUserToLoginAttemptCache(principal)
        }
    }
}