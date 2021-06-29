package com.project.dia.web_mvc.repository

import com.project.dia.web_mvc.Model.Entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Int> {

    fun countByEmailContains(email: String): Long
    fun findUserByEmailToken(token:String): UserEntity
    fun findUserByEmail(email: String): UserEntity
}