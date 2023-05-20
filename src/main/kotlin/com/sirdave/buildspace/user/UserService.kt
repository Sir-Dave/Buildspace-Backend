package com.sirdave.buildspace.user

import java.util.*

interface UserService {

    fun saveUser(user: User)

    fun findUserById(id: UUID): User

    fun findUserByEmail(email: String): User

    fun isUserDoesNotExist(email: String): Boolean
}