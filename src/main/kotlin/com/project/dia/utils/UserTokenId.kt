package com.project.dia.utils

import com.project.dia.web_mvc.constants.SecurityKey
 import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.*

@Component
class UserTokenId {
     var random: Random = SecureRandom()
    var securityKey: SecurityKey = SecurityKey()
    var code = "a0bpk9n8emr"
    fun generateUserId(length: Int): String {
        val userId: StringBuilder = StringBuilder(length)
        for (i in 1 until length) {
            userId.append(code.toCharArray((random.nextInt(code.length))))
        }
        return String(userId)
    }
    fun checkExpirationToken(token:String): Boolean {
        val claim:Claims= Jwts.parser().setSigningKey(securityKey.getSecretToken()).parseClaimsJws(token).body
        val expiration:Date=claim.expiration
        val newDate:Date= Date()
        return expiration.before(newDate)
    }
    fun generateEmailToken(userId:String):String{
        return Jwts.builder().setSubject(userId).setExpiration(Date(System.currentTimeMillis()+ securityKey.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, securityKey.getSecretToken()).compact()
    }
    fun generatePasswordToken(userId: String): String? {
        return Jwts.builder().setSubject(userId)
                .setExpiration(Date(System.currentTimeMillis()+1000*60*60))
                .signWith(SignatureAlgorithm.HS512, securityKey.getSecretToken())
                .compact()
    }


}