package com.project.dia

import com.project.dia.ApplicationContext.SpringApplicationContext
import com.project.dia.web_mvc.constants.EnvProprieties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

    @SpringBootApplication
class AplicationApplication{

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<AplicationApplication>(*args)

        }
    }
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder();
    }
    @Bean
    fun springApplicationContext(): SpringApplicationContext {

        return SpringApplicationContext()
    }
    @Bean(name = ["EnvProprieties"])
    fun getEnvProprieties(): EnvProprieties {
        return EnvProprieties()
    }
}