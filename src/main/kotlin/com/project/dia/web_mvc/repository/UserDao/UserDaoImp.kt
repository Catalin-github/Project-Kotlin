package com.project.dia.web_mvc.repository.UserDao

import com.project.dia.web_mvc.Model.Entity.UserEntity
import com.project.dia.web_mvc.repository.UserJpaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserDaoImp : UserDao {
    @Autowired
     lateinit var  userJpaRepository: UserJpaRepository



    override fun checkUserEmailExist(email: String ): Long {
        println("123")
       return userJpaRepository.countByEmailContains(email)
    }

    override fun findAll(): MutableList<UserEntity> {
        return userJpaRepository.findAll()
    }

    override fun saveUser(user: UserEntity): UserEntity {
        return userJpaRepository.save(user)
    }

    override fun findUserByEmail(email: String): UserEntity {
        return userJpaRepository.findUserByEmail(email)
    }

    override fun updateUser(user: UserEntity): UserEntity {
        return userJpaRepository.saveAndFlush(user)
    }

    override fun findUserByEmailToken(token: String): UserEntity {
        return userJpaRepository.findUserByEmailToken(token)
    }

}