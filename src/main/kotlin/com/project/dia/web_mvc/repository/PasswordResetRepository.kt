package com.project.dia.web_mvc.repository

import com.project.dia.web_mvc.Model.Entity.PasswordResetEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordResetRepository:JpaRepository<PasswordResetEntity,Long> {
    fun findByToken(token:String):PasswordResetEntity
}