package com.project.dia.web_mvc.Model.Model_Request

data class RegisterModel(
        var email:String, var firstName:String,
        var lastName:String, var password:String,
        var userId:String?=null,var phone:String?=null)
