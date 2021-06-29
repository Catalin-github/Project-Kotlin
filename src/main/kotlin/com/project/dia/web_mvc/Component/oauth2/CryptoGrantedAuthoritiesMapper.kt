package com.project.dia.web_mvc.Component.oauth2


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import java.util.*

open class CryptoGrantedAuthoritiesMapper : GrantedAuthoritiesMapper {
    override fun mapAuthorities(authorities: Collection<GrantedAuthority>): Collection<GrantedAuthority> {
        val mappedAuthorities: MutableSet<GrantedAuthority> = HashSet()
        authorities.forEach { authority: GrantedAuthority ->
            if (OidcUserAuthority::class.java.isInstance(authority)) {
                val oidcUserAuthority: OidcUserAuthority = authority as OidcUserAuthority
                val idToken: OidcIdToken = oidcUserAuthority.idToken
                val userInfo: OidcUserInfo = oidcUserAuthority.userInfo
                val userAttributes: Map<String, Any> = oidcUserAuthority.attributes
            } else if (OAuth2UserAuthority::class.java.isInstance(authority)) {
                val oauth2UserAuthority: OAuth2UserAuthority = authority as OAuth2UserAuthority
                val userAttributes: Map<String, Any> = oauth2UserAuthority.attributes
            }
            mappedAuthorities.add(SimpleGrantedAuthority("ROLE_GUEST"))
        }
        return mappedAuthorities
    }
}