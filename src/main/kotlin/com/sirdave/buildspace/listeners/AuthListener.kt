package com.sirdave.buildspace.listeners

import com.sirdave.buildspace.email.EmailService
import com.sirdave.buildspace.event.AuthEvent
import com.sirdave.buildspace.token.TokenService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@Component
class AuthListener(
    private val tokenService: TokenService,
    private val emailService: EmailService) {

    @EventListener
    fun onRegistrationSuccess(event: AuthEvent){
        val emailSubject = "Welcome To BuildSpace"

        val user = event.user
        val recipientAddress = user.email

        val token = tokenService.createVerificationToken(user)
        val confirmationUrl = "${event.appUrl}/confirm?token=${token.token}"

        println("confirmation url is $confirmationUrl")

        val body = """Hello ${user.firstName},
Welcome to BuildSpace. Please click here $confirmationUrl to verify your account. 
Please note that the verification link is only valid for 24 hours.


Have a nice day!
The Support Team,
BuildCodeTogether Limited"""

        emailService.sendEmailToUser(
            recipientAddress,
            emailSubject,
            body
        )
    }
}