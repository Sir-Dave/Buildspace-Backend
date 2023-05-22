package com.sirdave.buildspace.token

import com.sirdave.buildspace.exception.EntityNotFoundException
import com.sirdave.buildspace.user.User
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class TokenServiceImpl(private val repository: TokenRepository) : TokenService {

    override fun findByToken(token: String): Token{
        return repository.findByToken(token)
            .orElseThrow {
                EntityNotFoundException("Token does not exist")
            }
    }

    override fun createVerificationToken(user: User): Token {
        val token = UUID.randomUUID().toString()
        val confirmationToken = Token(
            token = token,
            createdAt = LocalDateTime.now(),
            expiresAt = LocalDateTime.now().plusHours(24),
            user = user
        )
        return repository.save(confirmationToken)
    }

    @Transactional
    override fun confirmToken(token: String){
        val confirmationToken = findByToken(token)
        val expiredAt = confirmationToken.expiresAt

        check(!expiredAt.isBefore(LocalDateTime.now())){
            "Token expired"
        }

        //only confirm the token if it has not already
        if (confirmationToken.confirmedAt == null){
            confirmationToken.confirmedAt = LocalDateTime.now()

            //Activate the user after confirming the token
            confirmationToken.user.isActive = true
        }
    }
}