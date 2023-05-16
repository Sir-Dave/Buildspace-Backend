package com.sirdave.buildspace.user

data class UserDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String?,
    val dateJoined: String,
    val role: String,
)