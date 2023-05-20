package com.sirdave.buildspace.mapper

import com.sirdave.buildspace.user.User
import com.sirdave.buildspace.user.UserDto
import java.time.format.DateTimeFormatter
import java.util.*

fun User.toUserDto(): UserDto {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val date = dateJoined.format(formatter)

    return UserDto(
        id = id.toString(),
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber  ?: "",
        dateJoined = date,
        role = role,
    )
}