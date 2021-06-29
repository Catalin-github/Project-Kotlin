package com.project.dia.web_mvc.config_security.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.dia.utils.JwtUtils
import com.project.dia.web_mvc.Model.Model_Request.LoginModel
import com.project.dia.web_mvc.config_security.UserPrincipal
import com.project.dia.web_mvc.constants.SecurityKey
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

class AuthenticationFilter(var autheticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    private var jwtUtils: JwtUtils = JwtUtils()
    private var securityKey: SecurityKey = SecurityKey()
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val objectMapper = ObjectMapper()

        try {

            val creds: LoginModel = objectMapper.readValue(request.inputStream, LoginModel::class.java)
            println(creds.email)
            println("creds.email")

            return autheticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    creds.email,
                    creds.password, ArrayList()
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.getContext().authentication = authResult
        val userName: String? = (authResult.principal as UserPrincipal).username
        val claims = HashMap<String, Any>()
        claims["account_type"] = securityKey.BASIC
        val token: String? = jwtUtils.generateToken(userName, claims)
        jwtUtils.addCookieToResponse(token, response)

        chain.doFilter(request, response)
    }


}


