package com.sirdave.buildspace.email

import com.sirdave.buildspace.user.User

interface EmailService {
    fun sendEmailToUser(user: User, emailSubject: String, body: String)
}