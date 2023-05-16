package com.sirdave.buildspace.user

import com.sirdave.buildspace.exception.EntityNotFoundException
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
}