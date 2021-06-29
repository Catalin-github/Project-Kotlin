package com.project.dia.web_mvc.Component

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class PasswordListener() : ApplicationListener<OnPasswordResetEvent> {
    val serverUrl = "http://localhost:3000"

    @Autowired
    lateinit var mailSender: JavaMailSender

    override fun onApplicationEvent(event: OnPasswordResetEvent) {
        this.resetPassword(event)
    }

    fun resetPassword(event: OnPasswordResetEvent) {
        val adress: String? = event.account.userDetails.email
        val subject = "Reset password"
        val confirmationUrl = "/reset-password/" + event.account.token
        val message = "Click the link for reset password"
        val email = SimpleMailMessage()
        email.setTo(adress)
        println(adress)
        println(confirmationUrl)
        println(event.account.token)

        if (adress != null) {
            email.setSubject(adress)
        }
        email.setSubject(subject)
        email.setText(message + "\r\n" + serverUrl + confirmationUrl)
        mailSender.send(email)
    }

}