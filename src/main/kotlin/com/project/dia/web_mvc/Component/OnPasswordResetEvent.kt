package com.project.dia.web_mvc.Component

import com.project.dia.web_mvc.Model.Entity.PasswordResetEntity
import org.springframework.context.ApplicationEvent

class OnPasswordResetEvent(var account: PasswordResetEntity, var appUrl:String):ApplicationEvent(account){

}