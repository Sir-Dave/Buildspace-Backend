package com.sirdave.buildspace.token

import com.sirdave.buildspace.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class TokenServiceImpl(private val repository: TokenRepository) : TokenService {

    override fun findByToken(token: String): Token{
        return repository.findByToken(token)
            .orElseThrow {
                EntityNotFoundException("Token does not exist")
            }
    }

    @Transactional
    override fun confirmToken(token: String): Token {
        val confirmationToken = findByToken(token)
        val expiredAt = confirmationToken.expiresAt

        check(!expiredAt.isBefore(LocalDateTime.now())){
            "Token expired"
        }

        if (confirmationToken.confirmedAt == null) //only confirm the token if it has not already
            confirmationToken.confirmedAt = LocalDateTime.now()

        return confirmationToken
    }

    override fun saveToken(token: Token){
        repository.save<Token>(token)
    }
}