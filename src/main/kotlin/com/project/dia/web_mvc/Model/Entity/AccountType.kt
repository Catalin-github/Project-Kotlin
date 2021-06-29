package com.project.dia.web_mvc.Model.Entity

import com.project.dia.web_mvc.Enum.AccountTypes
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name="account")
class AccountType(
        @Id @GeneratedValue(strategy = IDENTITY)
        @Column(name = "id")
        var id:Int=0,
        @Column(name = "type")
        var type: String,
        @ManyToOne(cascade = [CascadeType.MERGE])
        @JoinColumn(name = "user_id")
        var user:UserEntity?=null




)