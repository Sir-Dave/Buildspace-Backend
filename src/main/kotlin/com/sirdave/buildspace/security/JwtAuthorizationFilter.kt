package com.sirdave.buildspace.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sirdave.buildspace.constants.SecurityConstants
import com.sirdave.buildspace.helper.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

            try {
                val username = jwtTokenProvider.getSubject(token)
                if (jwtTokenProvider.isTokenValid(username, token) &&
                    SecurityContextHolder.getContext().authentication == null){
                    val authorities = jwtTokenProvider.getAuthorities(token)
                    val authentication = jwtTokenProvider.getAuthentication(username, authorities, request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
                else SecurityContextHolder.clearContext()
            }
            catch (exception: TokenExpiredException){
                val apiResponse = ApiResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED.reasonPhrase, "Token has expired, login to continue")
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                response.status = HttpStatus.UNAUTHORIZED.value()
                val outputStream = response.outputStream
                val mapper = ObjectMapper()
                mapper.writeValue(outputStream, apiResponse)
                outputStream.flush()
                return
            }


        }

        filterChain.doFilter(request, response)
    }
}