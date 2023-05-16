package com.sirdave.buildspace.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.sirdave.buildspace.constants.SecurityConstants
import com.sirdave.buildspace.helper.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint: Http403ForbiddenEntryPoint() {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse,
                          exception: AuthenticationException){

        val apiResponse = ApiResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,
            HttpStatus.FORBIDDEN.reasonPhrase, SecurityConstants.FORBIDDEN_MESSAGE)
        response.contentType = APPLICATION_JSON_VALUE
        response.status = HttpStatus.FORBIDDEN.value()
        val outputStream = response.outputStream
        val mapper = ObjectMapper()
        mapper.writeValue(outputStream, apiResponse)
        outputStream.flush()
    }
}