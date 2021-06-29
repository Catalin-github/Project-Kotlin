package com.project.dia.web_mvc.Model.Entity

import javax.persistence.*
@Entity(name="password_reset_tokens")
 class PasswordResetEntity(
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        val id:Long=0,

        val token: String?=null,

        @OneToOne
        @JoinColumn(name = "user_id")
        val userDetails:UserEntity

)