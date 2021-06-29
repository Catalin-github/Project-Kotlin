package com.project.dia.web_mvc.Component.oauth2

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CryptoOidcUserService(): OidcUserService() {

    override fun loadUser(userRequest: OidcUserRequest?): OidcUser {
        return OidcCryptoUser(super.loadUser(userRequest))
    }
}