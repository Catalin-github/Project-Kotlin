package com.project.dia.web_mvc.Model.Entity

import javax.persistence.*

@Entity
@Table(name = "authorities")
class AuthorityEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0

        @Column(nullable = false, length = 20)
        var name: String? = null

        @ManyToMany(mappedBy = "authorities")
        var roles: Collection<RolesEntity>? = null

        constructor() {}
        constructor(name: String?) {
                this.name = name
        }
}