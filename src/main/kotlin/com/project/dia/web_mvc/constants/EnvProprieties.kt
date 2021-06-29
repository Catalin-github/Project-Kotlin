package com.project.dia.web_mvc.constants

 import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.core.env.Environment
 import org.springframework.stereotype.Component

@Component
class EnvProprieties {
    @Autowired
    lateinit var env: Environment
    fun getGoogleId():String?{
        return env.getProperty("GOOGLE_ID")
    }
    fun getSecretToken(): String? {
        return env.getProperty("SECRET_TOKEN")
    }

}