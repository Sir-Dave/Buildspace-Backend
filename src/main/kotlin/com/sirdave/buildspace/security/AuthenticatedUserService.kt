package com.sirdave.buildspace.security

import com.sirdave.buildspace.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticatedUserService(private val userRepository: UserRepository) {

    fun hasId(userId: String): Boolean {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        val user  = userRepository.findByEmail(email).orElseThrow()
        return user.id.toString() == userId
    }
}