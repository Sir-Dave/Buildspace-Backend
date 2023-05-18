package com.sirdave.buildspace.auth

import com.sirdave.buildspace.user.UserDto

data class RegisterRequest(
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String?,
    var role: String,
    var password: String,
    var confirmPassword: String
)

data class SignInRequest(
    val email: String,
    val password: String,
)

data class SignInResponse(
    val token: String,
    val user: UserDto
)