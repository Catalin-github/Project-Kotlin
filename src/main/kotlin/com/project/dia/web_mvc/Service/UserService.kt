package com.project.dia.web_mvc.Service

import com.project.dia.web_mvc.Model.Entity.UserEntity
import com.project.dia.web_mvc.Model.Model_Request.FacebookModel
import com.project.dia.web_mvc.Model.Model_Request.GoogleModel
import com.project.dia.web_mvc.Model.Model_Request.RegisterModel
import com.project.dia.web_mvc.Model.Model_Request.UpdateDataModel
import com.project.dia.web_mvc.Model.Model_Response.ModelResponse
import org.springframework.security.core.userdetails.UserDetailsService
import javax.servlet.http.HttpServletResponse

interface UserService:UserDetailsService{
    fun findAllUser():MutableList<UserEntity>
    fun findUserByEmailAndSendResponse(email: String,response: HttpServletResponse): ModelResponse
    fun saveUser(user: RegisterModel): ModelResponse
    fun updateUser(user: UpdateDataModel): ModelResponse
    fun findUserByEmail(email: String):UserEntity
    fun validateEmailToken(token:String): ModelResponse
    fun requestResetPassword(email:String):ModelResponse
    fun accountData(email:String):ModelResponse
    fun resetPassword(token:String,password:String):ModelResponse
    fun registerWithGoogle(googleModel: GoogleModel,response: HttpServletResponse):ModelResponse
    fun updateUserData(userEntity: UserEntity)
    fun registerWithFacebook(facebookModel: FacebookModel, response: HttpServletResponse): ModelResponse
 }