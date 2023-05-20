package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.email.EmailService
import com.sirdave.buildspace.event.AuthEvent
import com.sirdave.buildspace.token.TokenService
import org.springframework.stereotype.Component


@Component
class AuthListener(
    private val tokenService: TokenService,
    private val emailService: EmailService
) {
    fun onRegistrationSuccess(event: AuthEvent){
        val emailSubject = "Registration Confirmation"

        val user = event.user
        val recipientAddress = user.email

        val token = tokenService.createVerificationToken(user)
        val confirmationUrl = "${event.appUrl}/registrationConfirm?token=${token.token}"

        // TODO: Put the appropriate body later on
        val body = ""

        emailService.sendEmailToUser(
            recipientAddress,
            emailSubject,
            body
        )
    }
}