package com.project.dia.web_mvc.config_security.filters

import com.project.dia.web_mvc.Model.Entity.UserEntity
import com.project.dia.web_mvc.config_security.UserPrincipal
import com.project.dia.web_mvc.constants.SecurityKey
import com.project.dia.web_mvc.repository.UserJpaRepository
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthorizationFilter(authManager: AuthenticationManager?, var userJpaRepository: UserJpaRepository) : BasicAuthenticationFilter(   authManager) {
    var securityKey: SecurityKey = SecurityKey()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        println("authorizarion")

        val cookieAuth: Optional<Cookie> = Stream.of(*Optional.ofNullable(request.cookies).orElse(arrayOfNulls<Cookie>(0)))
                .filter(Predicate { cookie: Cookie -> securityKey.HEADER_NAME == cookie.name })
                .findFirst()
         if (!cookieAuth.isPresent) {
             println("here")
            return;
        }
        val authentication: UsernamePasswordAuthenticationToken? = getAuthentication(request)
        println("context flow")

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {

        val cookieAuth: Optional<Cookie> = Stream.of(*Optional.ofNullable(request.cookies).orElse(arrayOfNulls<Cookie>(0)))
                .filter(Predicate { cookie: Cookie -> securityKey.HEADER_NAME == cookie.name })
                .findFirst()
        if (cookieAuth.isPresent) {

            println(cookieAuth.get().value)
            val user: String? = Jwts.parser().setSigningKey(securityKey.getSecretToken()).parseClaimsJws(cookieAuth.get().value).body.subject
            val typeAccount= Jwts.parser().setSigningKey(securityKey.getSecretToken()).parseClaimsJws(cookieAuth.get().value).body["account_type"]

            println(typeAccount)
            println("tyszdasdadsadsadasdasdasdaspeAccount")

            if (user != null) {

                val userEntity: UserEntity = userJpaRepository.findUserByEmail(email = user) ?: return null
                val userPrincipal: UserPrincipal = UserPrincipal(userEntity = userEntity)

                return UsernamePasswordAuthenticationToken(  userPrincipal, null, userPrincipal.authorities)
            }
            return null;
        }
        return null
    }

     override fun shouldNotFilter(request: HttpServletRequest): Boolean {
         println(request.requestURI in listOf("/dia-aplication/api/user/accountConfirm", "/dia-aplication/api/user/reset-password", "/dia-aplication/api/user/request-reset-password", "/dia-aplication/api/user/login", "/dia-aplication/api/user/register", "/dia-aplication/api/user/loginlogin"))
         return request.requestURI in listOf(
                 "/dia-aplication/api/user/accountConfirm",
                 "/dia-aplication/api/user/reset-password",
                 "/dia-aplication/api/user/request-reset-password",
                 "/dia-aplication/api/user/login",
                 "/dia-aplication/api/user/register",
                 "/dia-aplication/api/user/loginlogin",
                 "/dia-aplication/api/user/google",
                 "/dia-aplication/api/user/facebook")

    }
}