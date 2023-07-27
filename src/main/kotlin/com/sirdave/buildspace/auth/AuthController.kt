package com.sirdave.buildspace.auth

import com.sirdave.buildspace.helper.ApiResponse
import com.sirdave.buildspace.token.TokenService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = ["api/v1/auth"])
class AuthController(
    private val authService: AuthService,
    private val tokenService: TokenService
) {

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    fun registerUser(
        @RequestBody registerRequest: RegisterRequest,
        servletRequest: HttpServletRequest
    ): ResponseEntity<ApiResponse> {
        authService.register(registerRequest, servletRequest)
        val response = ApiResponse(
            HttpStatus.CREATED.value(),
            HttpStatus.CREATED,
            HttpStatus.CREATED.reasonPhrase,
            "Registration successful. Please check your email for a verification link."
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @Operation(summary = "Activate user using their email address")
    @GetMapping("/register/confirm")
    fun confirmUserToken(@RequestParam("token") token: String): ResponseEntity<ApiResponse> {
        tokenService.confirmToken(token)
        val response = ApiResponse(
            HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.reasonPhrase,
            "Account verified. Please proceed to log in to your account."
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    fun loginUser(@RequestBody signInRequest: SignInRequest): ResponseEntity<SignInResponse> {
        val response = authService.login(signInRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }
}