package com.project.dia.web_mvc.config_security

import com.project.dia.web_mvc.Model.Entity.AuthorityEntity
import com.project.dia.web_mvc.Model.Entity.RolesEntity
import com.project.dia.web_mvc.Model.Entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.function.Consumer

class UserPrincipal(var userEntity: UserEntity):UserDetails {

     override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities:MutableCollection<GrantedAuthority> = HashSet()
        val authorityEntities:MutableCollection<AuthorityEntity> = HashSet()
        val roles: Collection<RolesEntity> = userEntity.roles ?: return authorities
         roles.forEach(Consumer { role: RolesEntity?->
             authorities.add(SimpleGrantedAuthority(role?.name))
             role?.authorities?.let { authorityEntities.addAll(it) }
         })
        authorityEntities.forEach(Consumer { authorityEntity: AuthorityEntity ->
            authorities.add(SimpleGrantedAuthority(authorityEntity.name))
        })
        return authorities
         }



    override fun getPassword(): String? {
        return userEntity.password
    }


    override fun getUsername(): String? {
        return userEntity.email
    }


    override fun isAccountNonExpired(): Boolean {
        return true
    }


    override fun isAccountNonLocked(): Boolean {
        return true
    }


    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return userEntity.emailStatus
    }
}