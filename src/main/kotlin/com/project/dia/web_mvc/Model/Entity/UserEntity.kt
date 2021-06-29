package com.project.dia.web_mvc.Model.Entity

import com.project.dia.web_mvc.Enum.AccountTypes
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "user")
class UserEntity(
        @Id @GeneratedValue(strategy= IDENTITY)
        @Column(name="id")
        var id: Int =0,
        @Column(name = "UserId")
        var UserId:String ,
        @Column
        (name="email")
        var email:String?=null,
        @Column
        (name="first_name",nullable = false)
        var firstName:String?=null,
        @Column
        (name="last_name",nullable = false)
        var lastName: String?=null,
        @Column
        (name="password")
        var password:String?=null,
        @Column(name="phone")
        var phone:String?=null,
        @Column(nullable = true,columnDefinition = "boolean default false")
         var emailStatus:Boolean,
         var emailToken:String?=null,
        @Column(name="login")
        var login:String?="basic",
        @ManyToMany(cascade = [PERSIST],fetch = FetchType.EAGER)
        @JoinTable(name="users_roles",
                joinColumns = [JoinColumn(name = "users_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "roles_id", referencedColumnName = "id")])
          var roles:Collection<RolesEntity>?=null   ,
        @OneToMany(mappedBy = "user",cascade = [PERSIST,DETACH,MERGE])
        var accountType: MutableList<AccountType>?=null
){
        fun add(account: AccountType){
                if (accountType==null){
                        accountType= mutableListOf()
                }
                accountType!!.add(AccountType(type = account.type,user = account.user ))
        }
}