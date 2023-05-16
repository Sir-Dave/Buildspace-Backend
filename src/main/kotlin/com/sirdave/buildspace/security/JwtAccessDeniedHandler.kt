package com.company.arena.security

import com.company.arena.constants.SecurityConstants
import com.company.arena.helper.HttpResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAccessDeniedHandler: AccessDeniedHandler {
    override fun handle(request: HttpServletRequest, response: HttpServletResponse, exception: AccessDeniedException?) {
        val httpResponse = HttpResponse(
            HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.reasonPhrase, SecurityConstants.ACCESS_DENIED)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val outputStream = response.outputStream
        val mapper = ObjectMapper()
        mapper.writeValue(outputStream, httpResponse)
        outputStream.flush()
    }
}