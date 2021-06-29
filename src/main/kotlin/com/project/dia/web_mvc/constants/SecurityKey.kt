package com.project.dia.web_mvc.constants
import com.project.dia.ApplicationContext.SpringApplicationContext

class SecurityKey(val EXPIRATION_TIME:Long=86400000,
                  val TOKEN_NAME:String="Bearer ",
                  val HEADER_NAME:String="Authorization",
                  val BASIC:String="basic",
                  val FACEBOOK:String="facebook",
                  val GOOGLE:String="google"
                   ){
    fun getGoogleId():String?{
        val envProprieties: EnvProprieties = SpringApplicationContext.getBean("EnvProprieties")
                as EnvProprieties
        return envProprieties.getGoogleId()
    }
    fun getSecretToken(): String? {

        val envProprieties: EnvProprieties = SpringApplicationContext.getBean("EnvProprieties")
                as EnvProprieties

        return envProprieties.getSecretToken()}
 }