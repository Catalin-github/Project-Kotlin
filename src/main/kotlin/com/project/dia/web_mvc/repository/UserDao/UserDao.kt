package com.project.dia.web_mvc.repository.UserDao

import com.project.dia.web_mvc.Model.Entity.UserEntity

interface UserDao {
    fun checkUserEmailExist(email: String ):Long
    fun findAll():MutableList<UserEntity>
    fun saveUser(user: UserEntity): UserEntity
    fun findUserByEmail(email:String): UserEntity
    fun updateUser(user: UserEntity): UserEntity
    fun findUserByEmailToken(token:String): UserEntity
}