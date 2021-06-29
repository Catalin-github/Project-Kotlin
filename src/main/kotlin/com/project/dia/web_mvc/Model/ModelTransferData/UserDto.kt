package com.project.dia.web_mvc.Model.ModelTransferData

data class UserDto(var id:Int?=null,
                   var  firstName:String?=null,
                   var lastName:String?=null,
                   var email:String?=null,
                   var password:String?=null,
                   var encryptedPassword:String?=null,
                   var emaiVerificationToken:String?=null,
                   var emailVerificationStatus:Boolean?=null,
                   var roles:Collection<String>?=null);
