package com.project.dia.web_mvc.Model.Entity

import javax.persistence.*
import javax.persistence.GenerationType.AUTO

@Entity
@Table(name="roles")
class RolesEntity  {

        @Id @GeneratedValue(strategy = AUTO)
        var id:Long=0

        @Column(nullable = false, length = 20)
        var name:String?=null
        @ManyToMany(mappedBy = "roles")
        var users:Collection<UserEntity>?=null

        @ManyToMany(cascade = [CascadeType.PERSIST],fetch = FetchType.EAGER)
        @JoinTable(name="roles_authorities",
                joinColumns = [JoinColumn(name = "roles_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "authorities_id", referencedColumnName = "id")])
         var authorities:Collection<AuthorityEntity>?=null
        constructor() {}
        constructor(name: String?) {
                this.name = name
        }
}

