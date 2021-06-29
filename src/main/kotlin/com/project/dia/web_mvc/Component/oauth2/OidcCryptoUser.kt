package com.project.dia.web_mvc.Component.oauth2

import com.project.dia.web_mvc.Model.Model_Request.CryptoAuthenticatedPrincipal
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class OidcCryptoUser(user: OidcUser) : DefaultOidcUser(user.authorities, user.idToken), CryptoAuthenticatedPrincipal {


    override fun firstName(): String? {
        return this.attributes["given_name"].toString()
    }

    override fun lastName(): String? {
        return this.attributes["family_name"].toString()
    }

    override fun firstNameAndLastName(): String? {
        return lastName() + " " + lastName()
    }


}