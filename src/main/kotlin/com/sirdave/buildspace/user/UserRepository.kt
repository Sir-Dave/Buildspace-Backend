package com.sirdave.buildspace.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID>{

    @Query("SELECT u FROM User u")
    fun getAllUsers(pageable: Pageable): Page<User>

    fun findByEmail(email: String): Optional<User>
}