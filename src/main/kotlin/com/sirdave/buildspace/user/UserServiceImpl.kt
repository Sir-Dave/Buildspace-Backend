package com.sirdave.buildspace.user

import com.sirdave.buildspace.constants.AuthorityConstants.USER_READ
import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.mapper.toUserDto
import org.springframework.data.domain.PageRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository) : UserService{

    override fun saveUser(user: User) {
        userRepository.save(user)
    }

    override fun findUserById(id: UUID): User {
        return userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No user with id $id was found") }
    }

    override fun findUserByEmail(email: String): User {
        return userRepository.findByEmail(email)
            .orElseThrow { EntityNotFoundException("No user with email $email was found") }

    }

    @Transactional
    @PreAuthorize("@authenticatedUserService.hasId(#id)")
    override fun updateUser(id: UUID, firstName: String?,
                            lastName: String?, phoneNumber: String?): User {
        val user = findUserById(id)
        if (!firstName.isNullOrBlank())
            user.firstName = firstName

        if (!lastName.isNullOrBlank())
            user.lastName = lastName

        if (!phoneNumber.isNullOrBlank())
            user.phoneNumber = phoneNumber

        return user
    }

    override fun isUserDoesNotExist(email: String): Boolean {
        val userByEmail = userRepository.findByEmail(email)
        return userByEmail.isEmpty
    }

    @PreAuthorize("hasAnyAuthority('${USER_READ}')")
    override fun getAllUsers(page: Int, pageSize: Int): List<UserDto> {
        val pageable = PageRequest.of(page, pageSize)
        val pagedResult = userRepository.getAllUsers(pageable)
        return if (pagedResult.hasContent()) pagedResult.content.map { it.toUserDto() } else emptyList()
    }

    override fun retrieveAllUsers(): List<User> {
        return userRepository.findAll()
    }
}