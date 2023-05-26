package com.sirdave.buildspace.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import com.sirdave.buildspace.helper.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.*


private const val ACCOUNT_LOCKED = "Your account has been locked. Please contact administration"
private const val METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request"
private const val INCORRECT_CREDENTIALS = "Username/password incorrect, please try again"
private const val ACCOUNT_DISABLED = "Please verify your account to continue. If this is an error, please contact administration"
private const val NOT_ENOUGH_PERMISSIONS = "You do not have sufficient permissions to access this endpoint"
private const val ERROR_PATH = "/error"

@RestControllerAdvice
class ExceptionHandling: ErrorController {
    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(DisabledException::class)
    fun accountDisabledException(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.UNAUTHORIZED, NOT_ENOUGH_PERMISSIONS)
    }

    @ExceptionHandler(LockedException::class)
    fun accountLockedException(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun tokenExpiredException(exception: TokenExpiredException): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.message!!)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun entityNotFoundException(exception: EntityNotFoundException): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.message!!)
    }


    @ExceptionHandler(EntityExistsException::class)
    fun entityExistsException(exception: EntityExistsException): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(PasswordsDoNotMatchException::class)
    fun passwordsDoNotMatchException(exception: PasswordsDoNotMatchException): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun methodNotSupportedException(exception: HttpRequestMethodNotSupportedException): ResponseEntity<ApiResponse>{
        val supportedMethod = Objects.requireNonNull(exception.supportedHttpMethods).iterator().next()
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun illegalStateException(exception: IllegalStateException): ResponseEntity<ApiResponse>{
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(PaymentException::class)
    fun paymentException(exception: PaymentException): ResponseEntity<ApiResponse>{
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.message!!)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<ApiResponse>{
        LOGGER.error(exception.message)
        return createHttpResponse(HttpStatus.BAD_REQUEST, "Invalid request payload")
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorException(exception: Exception): ResponseEntity<ApiResponse>{
        LOGGER.info("type is ${exception.javaClass.name}")
        LOGGER.error(exception.message)

        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.message!!)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun noHandlerFoundException(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.NOT_FOUND, "This page was not found")
    }

    @RequestMapping(ERROR_PATH)
    fun notFound404(): ResponseEntity<ApiResponse>{
        return createHttpResponse(HttpStatus.NOT_FOUND, "No mapping found for this url")
    }

    private fun createHttpResponse(status: HttpStatus, message: String): ResponseEntity<ApiResponse>{
        val httpResponse = ApiResponse(status.value(), status,
            status.reasonPhrase.uppercase(), message)
        return ResponseEntity(httpResponse, status)
    }

    override fun getErrorPath(): String {
        return ERROR_PATH
    }
}






