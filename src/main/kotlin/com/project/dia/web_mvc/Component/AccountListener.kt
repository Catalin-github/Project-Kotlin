package com.project.dia.web_mvc.Component

import com.project.dia.utils.UserTokenId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class AccountListener:ApplicationListener<OnCreateAccountEvent> {
 val serverUrl="http://localhost:8443"

    @Autowired
    lateinit var userId: UserTokenId

    @Autowired
    lateinit var mailSender:JavaMailSender
    override fun onApplicationEvent(event: OnCreateAccountEvent) {
        this.confirmCreateAccount(event)
    }
    private fun confirmCreateAccount(event: OnCreateAccountEvent){
        val adress=event.account.email
        var token=event.account.emailToken
        val subject="Account Confirmation"
        println(adress)
        val confirmationUrl=event.appUrl+"/api/user/accountConfirm?token="+token
        val message="Please confirm"
        val mail: SimpleMailMessage= SimpleMailMessage()
        mail.setTo(adress)
        mail.setSubject(subject)
        println(adress)
        println(message+"\r\n"+serverUrl+confirmationUrl)

        mail.setText(message+"\r\n"+serverUrl+confirmationUrl)
        mailSender.send(mail)
    }


}