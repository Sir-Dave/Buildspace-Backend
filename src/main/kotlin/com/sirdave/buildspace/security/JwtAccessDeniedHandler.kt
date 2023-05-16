package com.sirdave.buildspace.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.sirdave.buildspace.constants.SecurityConstants
import com.sirdave.buildspace.helper.ApiResponse
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
        val apiResponse = ApiResponse(
            HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED.reasonPhrase, SecurityConstants.ACCESS_DENIED)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        val outputStream = response.outputStream
        val mapper = ObjectMapper()
        mapper.writeValue(outputStream, apiResponse)
        outputStream.flush()
    }
}