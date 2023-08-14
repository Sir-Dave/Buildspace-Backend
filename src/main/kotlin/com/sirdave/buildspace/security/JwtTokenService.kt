package com.sirdave.buildspace.security

import com.sirdave.buildspace.event.LogoutSuccessEvent
import net.jodah.expiringmap.ExpiringMap
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class JwtTokenService (private val jwtTokenProvider: JwtTokenProvider) {
    private val tokenEventMap: ExpiringMap<String, LogoutSuccessEvent> = ExpiringMap.builder()
        .variableExpiration()
        .maxSize(1000)
        .build()

    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun setTokenAsExpired(token: String) {
        if (tokenEventMap.containsKey(token)) {
            val subject = getLogoutEventForToken(token)!!.userEmail
            logger.info("Log out token for user $subject is already present in the cache")
        }
        else {
            val subject = jwtTokenProvider.getSubject(token)
            val tokenExpiryDate = jwtTokenProvider.getExpirationDate(token)
            val ttlForToken = getTTLForToken(tokenExpiryDate)
            val event = LogoutSuccessEvent(subject, token)
            logger.info("Logout token cache set for $subject with a TTL of $ttlForToken seconds. Token is due expiry at $tokenExpiryDate")
            tokenEventMap.put(token, event, ttlForToken, TimeUnit.SECONDS)
        }
    }

    fun getLogoutEventForToken(token: String): LogoutSuccessEvent? {
        return tokenEventMap[token]
    }

    private fun getTTLForToken(date: Date): Long {
        val secondsAtExpiry = date.toInstant().epochSecond
        val secondsAtLogout = Instant.now().epochSecond
        return 0L.coerceAtLeast(secondsAtExpiry - secondsAtLogout)
    }
}
