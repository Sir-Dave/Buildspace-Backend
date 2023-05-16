package com.company.arena.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.auth0.jwt.exceptions.JWTVerificationException
import com.company.arena.constants.SecurityConstants
import com.company.arena.user.UserPrincipal
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import java.util.*
import java.util.Arrays.stream
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateJwtToken(userPrincipal: UserPrincipal): String{
        val claims = getClaimsFromUser(userPrincipal)
        return JWT.create().withIssuer(SecurityConstants.ARENA_LLC)
            .withAudience(SecurityConstants.ARENA_ADMINISTRATION)
            .withIssuedAt(Date()).withSubject(userPrincipal.username)
            .withArrayClaim(SecurityConstants.AUTHORITIES, claims)
            .withExpiresAt(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_DATE))
            .sign(HMAC512(secret!!.toByteArray()))
    }

    fun getAuthorities(token: String): List<GrantedAuthority>{
        val claims = getClaimsFromToken(token)
        return stream(claims).map { s ->
            SimpleGrantedAuthority(s)
        }.collect(Collectors.toList())
    }

    fun getAuthentication(username: String, authorities: List<GrantedAuthority>,
                          request: HttpServletRequest): Authentication{

        val authToken = UsernamePasswordAuthenticationToken(username, null, authorities)
        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        return authToken
    }

    fun isTokenValid(username: String, token: String): Boolean{
        val verifier = getJWTVerifier()
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier, token)
    }

    fun getSubject(token: String): String {
        val verifier = getJWTVerifier()
        return verifier.verify(token).subject
    }

    private fun isTokenExpired(verifier: JWTVerifier, token: String): Boolean{
        val expirationDate = verifier.verify(token).expiresAt
        return expirationDate.before(Date())
    }

    private fun getClaimsFromUser(userPrincipal: UserPrincipal): Array<String>{
        val authorities = ArrayList<String>()
        for(grantedAuthorities in userPrincipal.authorities!!){
            authorities.add(grantedAuthorities!!.authority)
        }
        return authorities.toTypedArray()
    }

    private fun getClaimsFromToken(token: String): Array<String>{
        val verifier = getJWTVerifier()
        return verifier.verify(token).getClaim(SecurityConstants.AUTHORITIES).asArray(String::class.java)
    }

    private fun getJWTVerifier(): JWTVerifier{
        val verifier: JWTVerifier
        try {
            val algorithm = HMAC512(secret)
            verifier = JWT.require(algorithm).withIssuer(SecurityConstants.ARENA_LLC).build()
        }
        catch (exception: JWTVerificationException){
            throw JWTVerificationException(SecurityConstants.TOKEN_CANNOT_BE_VERIFIED)
        }
        return verifier
    }
}