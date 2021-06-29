package com.project.dia.web_mvc.repository

import com.project.dia.web_mvc.Model.Entity.AuthorityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorityRepository:JpaRepository<AuthorityEntity,Long> {
    fun findByName(name:String?): AuthorityEntity?
}