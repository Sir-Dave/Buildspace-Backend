package com.sirdave.buildspace.token

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenRepository : JpaRepository<Token, Long> {
    fun findByToken(token: String): Optional<Token>
}