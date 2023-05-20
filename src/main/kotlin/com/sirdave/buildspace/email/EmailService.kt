package com.sirdave.buildspace.email

interface EmailService {
    fun sendEmailToUser(email: String, emailSubject: String, body: String)
}