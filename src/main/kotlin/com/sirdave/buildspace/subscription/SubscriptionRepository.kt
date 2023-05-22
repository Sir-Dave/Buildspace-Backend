package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, UUID> {

    @Query("SELECT s FROM Subscription s WHERE s.user = ?1 ORDER BY s.startDate DESC")
    fun findAllByUser(user: User): Set<Subscription>
}