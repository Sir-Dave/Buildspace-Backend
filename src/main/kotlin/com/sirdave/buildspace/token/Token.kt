package com.sirdave.buildspace.token

import com.sirdave.buildspace.user.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tokens")
class Token(
    @Column(nullable = false)
    val token: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    val user: User

) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_sequence")
    @SequenceGenerator(name = "token_sequence", sequenceName = "token_sequence", allocationSize = 1)
    @Column(nullable = false, updatable = false)
    val id: Long? = null

    var confirmedAt: LocalDateTime? = null
}