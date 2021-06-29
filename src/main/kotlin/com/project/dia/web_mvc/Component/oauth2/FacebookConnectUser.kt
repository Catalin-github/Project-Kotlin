package com.project.dia.web_mvc.Component.oauth2

import com.project.dia.web_mvc.Model.Model_Request.CryptoAuthenticatedPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.core.user.OAuth2User



  class FacebookConnectUser(
          private var name: String,
          private val id: String,
          private val email: String,
          private val authorities: List<GrantedAuthority> = AuthorityUtils.createAuthorityList("ROLE_USER"),
          private var attributes: HashMap<String, Any> =HashMap<String, Any>(), firstName: String?, lastName: String?, firstNameAndLastName: String?, getEmail: String?
  )
      : OAuth2User, CryptoAuthenticatedPrincipal  {


    override fun getName(): String {
        return name
    }


    override fun getAttributes(): MutableMap<String, Any> {
        attributes["id"] = id
        attributes["name"] = getName()
        attributes["email"]=email
        attributes["given_name"]=getName().split(" ")[0]
        attributes["family_name"]=getName().split(" ")[1]
        return attributes
    }


      override fun getAuthorities(): List<GrantedAuthority> {
          return authorities
      }

      override fun firstName(): String? {
          return attributes["given_name"].toString()
      }

      override fun lastName(): String? {
          return attributes["family_name"].toString()
      }

      override fun firstNameAndLastName(): String? {
          return getName()
      }

      override fun getEmail(): String? {
          return email
      }


  }

