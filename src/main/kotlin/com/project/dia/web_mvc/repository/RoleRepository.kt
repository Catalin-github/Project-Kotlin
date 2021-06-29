package com.project.dia.web_mvc.repository

import com.project.dia.web_mvc.Model.Entity.RolesEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository:JpaRepository<RolesEntity,Long>{
    fun findByName(name:String?): RolesEntity?
}