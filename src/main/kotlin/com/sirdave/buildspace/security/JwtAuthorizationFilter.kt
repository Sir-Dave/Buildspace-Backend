package com.sirdave.buildspace.security

import com.sirdave.buildspace.constants.SecurityConstants
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthorizationFilter(private val jwtTokenProvider: JwtTokenProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (request.method.equals(SecurityConstants.OPTIONS_HTTP_METHOD, ignoreCase = true))
            response.status = HttpStatus.OK.value()
        else{
            val header = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response)
                return
            }
            val token = header.substring(SecurityConstants.TOKEN_PREFIX.length)
            val username = jwtTokenProvider.getSubject(token)
            if (jwtTokenProvider.isTokenValid(username, token) &&
                SecurityContextHolder.getContext().authentication == null){
                val authorities = jwtTokenProvider.getAuthorities(token)
                val authentication = jwtTokenProvider.getAuthentication(username, authorities, request)
                SecurityContextHolder.getContext().authentication = authentication
            }
            else SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}