package com.project.dia.utils

import com.project.dia.web_mvc.constants.SecurityKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.function.Function
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


class JwtUtils {
    var securityKey: SecurityKey = SecurityKey()

    private fun extractExpiration(token: String?): Date {
        return extractClaim(token) { obj: Claims -> obj.expiration }
    }

    private fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(securityKey.getSecretToken())
                .parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean? {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userName: String?, claims:HashMap<String,Any>): String? {

        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setExpiration(Date(System.currentTimeMillis() + securityKey.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, securityKey.getSecretToken())
                .compact()
    }
    fun addCookieToResponse(token: String?,response: HttpServletResponse) {

        val authCookie = Cookie(securityKey.HEADER_NAME, token)
        authCookie.isHttpOnly = true
        authCookie.maxAge = Duration.of(1, ChronoUnit.DAYS).toSeconds().toInt()
        authCookie.path = "/"

      return  response.addCookie(authCookie)

    }

}