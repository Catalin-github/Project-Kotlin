package com.project.dia.web_mvc.Model.Model_Request

interface CryptoAuthenticatedPrincipal {
    fun firstName(): String?
    fun lastName(): String?
    fun firstNameAndLastName(): String?
    fun getEmail(): String?
}