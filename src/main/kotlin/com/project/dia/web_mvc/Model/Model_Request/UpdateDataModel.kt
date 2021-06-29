package com.project.dia.web_mvc.Model.Model_Request

data class UpdateDataModel(
        var email: String ,
        var firstName: String? = null,
        var lastName: String? = null,
        var phone: String? = null,
)
