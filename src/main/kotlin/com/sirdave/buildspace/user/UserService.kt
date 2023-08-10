package com.sirdave.buildspace.user

import java.util.*

interface UserService {

    fun saveUser(user: User)

    fun findUserById(id: UUID): User

    fun findUserByEmail(email: String): User

    fun updateUser(id: UUID, firstName: String?,
                   lastName: String?, phoneNumber: String?): User

    fun isUserDoesNotExist(email: String): Boolean

    fun getAllUsers(page: Int, pageSize: Int): List<UserDto>

    fun retrieveAllUsers(): List<User>
}