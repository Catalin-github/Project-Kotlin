package com.project.dia.web_mvc.Component

import com.project.dia.web_mvc.Model.Entity.UserEntity
import org.springframework.context.ApplicationEvent

class OnCreateAccountEvent(var appUrl:String,
                           var account: UserEntity):ApplicationEvent(account) {

}