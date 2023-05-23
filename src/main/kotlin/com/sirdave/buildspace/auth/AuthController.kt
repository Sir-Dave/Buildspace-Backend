package com.sirdave.buildspace.auth

import com.sirdave.buildspace.helper.ApiResponse
import com.sirdave.buildspace.mapper.toUserDto
import com.sirdave.buildspace.security.JwtTokenProvider
import com.sirdave.buildspace.token.TokenService
import com.sirdave.buildspace.user.UserPrincipal
import com.sirdave.buildspace.user.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = ["api/v1/auth"])
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager,
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
        authenticateUser(signInRequest.email, signInRequest.password)
        val user = userService.findUserByEmail(signInRequest.email)

        val userPrincipal = UserPrincipal(user)
        val token = jwtTokenProvider.generateJwtToken(userPrincipal)
        val response = SignInResponse(token, user.toUserDto())
        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun authenticateUser(email: String, password: String){
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, password)
        )
    }
}