package com.sirdave.buildspace.email

import com.sirdave.buildspace.user.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class EmailServiceImpl : EmailService {

    @Value("\${email.username}")
    private val emailUsername: String? = null

    @Value("\${email.password}")
    private val emailPassword: String? = null
    private val logger = LoggerFactory.getLogger(javaClass)

    companion object{
        const val SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps"
        const val GMAIL_SMTP_SERVER = "smtp.gmail.com"
        const val SMTP_HOST = "mail.smtp.host"
        const val SMTP_AUTH = "mail.smtp.auth"
        const val SMTP_PORT = "mail.smtp.port"
        const val DEFAULT_PORT = 465
        const val SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable"
        const val SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required"
    }

    @Async
    override fun sendEmailToUser(user: User, emailSubject: String, body: String) {
        logger.info("Sending email to user...")
        try {
            val message = createMessage(user, emailSubject, body)
            val smtpTransport: Transport = emailSession
                .getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL)
            smtpTransport.connect(
                GMAIL_SMTP_SERVER, emailUsername,
                emailPassword
            )
            smtpTransport.sendMessage(message, message.allRecipients)
            smtpTransport.close()
            logger.info("Email sent to: " + user.email)
        } catch (exception: MessagingException) {
            logger.error("Failed to send message: " + exception.message)
            throw IllegalStateException("Failed to send email")
        }
    }


    private fun createMessage(user: User, emailSubject: String, body: String): Message {
        val message = MimeMessage(emailSession)
        message.setFrom(InternetAddress(emailUsername))
        message.setRecipient(Message.RecipientType.TO, InternetAddress(user.email, false))
        message.subject = emailSubject
        message.setText(body)
        message.sentDate = Date()
        message.saveChanges()
        return message
    }

    private val emailSession: Session
        get() {
            val properties = System.getProperties()
            properties[SMTP_HOST] = GMAIL_SMTP_SERVER
            properties[SMTP_AUTH] = true
            properties[SMTP_PORT] = DEFAULT_PORT
            properties[SMTP_STARTTLS_ENABLE] = true
            properties[SMTP_STARTTLS_REQUIRED] = true
            return Session.getInstance(properties, null)
        }
}