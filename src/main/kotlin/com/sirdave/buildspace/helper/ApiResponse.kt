package com.sirdave.buildspace.helper

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import java.util.*

class ApiResponse(httpStatusCode: Int, httpStatus: HttpStatus, reason: String, message: String) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Africa/Lagos")
    var timestamp: Date = Date()
    var httpStatusCode: Int? = httpStatusCode
    var httpStatus: HttpStatus? = httpStatus
    var reason: String? = reason
    var message: String? = message

    init {
        this.timestamp = Date()
    }
}