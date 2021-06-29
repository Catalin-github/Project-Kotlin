package com.project.dia.web_mvc.Component

import com.project.dia.web_mvc.Model.Entity.AuthorityEntity
import com.project.dia.web_mvc.Model.Entity.RolesEntity
import com.project.dia.web_mvc.repository.AuthorityRepository
import com.project.dia.web_mvc.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserUtils {
    @Autowired
    var authorityRepository: AuthorityRepository? = null

    @Autowired
    var rolesRepository: RoleRepository? = null

    @EventListener
    @Transactional
    fun onAplicationEvent(event: ApplicationReadyEvent?) {
        val readAuthority: AuthorityEntity = createAuthority("READ_AUTHORITY")
        val writeAuthority: AuthorityEntity = createAuthority("WRITE_AUTHORITY")
        val deleteAuthority: AuthorityEntity = createAuthority("DELETE_AUTHORITY")
        createRole("ROLE_GUEST", arrayListOf(readAuthority, writeAuthority))
        createRole("ROLE_ADMIN", arrayListOf(readAuthority, writeAuthority, deleteAuthority))

    }

    @Transactional
    fun createAuthority(name: String): AuthorityEntity {
        var authority = authorityRepository?.findByName(name)
        if (authority == null) {
            authority = AuthorityEntity(name)
            authorityRepository!!.save(authority)
        }
        return authority
    }

    @Transactional
    fun createRole(name: String, authorities: Collection<AuthorityEntity>): RolesEntity {
        var role: RolesEntity? = rolesRepository?.findByName(name)
        if (role == null) {
            role = RolesEntity(name)
            role.authorities = authorities
            rolesRepository?.save(role)

        }
        return role
    }

}