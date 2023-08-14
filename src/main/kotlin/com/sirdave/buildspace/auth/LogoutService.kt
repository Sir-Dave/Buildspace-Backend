package com.sirdave.buildspace.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.sirdave.buildspace.constants.SecurityConstants
import com.sirdave.buildspace.helper.ApiResponse
import com.sirdave.buildspace.security.JwtTokenService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogoutService(private val jwtTokenService: JwtTokenService): LogoutHandler {
    override fun logout(request: HttpServletRequest?,
                        response: HttpServletResponse?,
                        authentication: Authentication?) {

        val header = request?.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return
        }
        val token = header.substring(SecurityConstants.TOKEN_PREFIX.length)
        jwtTokenService.setTokenAsExpired(token)

        val apiResponse = ApiResponse(
            HttpStatus.OK.value(), HttpStatus.OK,
            HttpStatus.OK.reasonPhrase, "Logout successful")

        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpStatus.OK.value()

        val outputStream = response?.outputStream
        val mapper = ObjectMapper()
        mapper.writeValue(outputStream, apiResponse)
        outputStream?.flush()
        return
    }
}