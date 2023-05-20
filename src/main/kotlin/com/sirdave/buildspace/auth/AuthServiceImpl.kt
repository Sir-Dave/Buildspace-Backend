package com.sirdave.buildspace.auth

import com.sirdave.buildspace.event.AuthEvent
import com.sirdave.buildspace.exception.EntityExistsException
import com.sirdave.buildspace.exception.PasswordsDoNotMatchException
import com.sirdave.buildspace.helper.Role
import com.sirdave.buildspace.user.User
import com.sirdave.buildspace.user.UserPrincipal
import com.sirdave.buildspace.user.UserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
@Qualifier("userDetailsService")
class AuthServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val publisher: ApplicationEventPublisher
) : AuthService, UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userService.findUserByEmail(email)
        userService.saveUser(user)
        return UserPrincipal(user)
    }

    override fun register(registerRequest: RegisterRequest, servletRequest: HttpServletRequest) {
        validateNewUser(registerRequest.email)
        doPasswordsMatch(registerRequest.password, registerRequest.confirmPassword)

        val encodedPassword = encodePassword(registerRequest.password)
        val role = getRoleName(registerRequest.role)

        val user = User(
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            phoneNumber = registerRequest.phoneNumber,
            password = encodedPassword,
            dateJoined = LocalDateTime.now(),
            role = role.name,
            authorities = role.authorities,
            isActive = true,
            isNotLocked = true
        )
        userService.saveUser(user)
        publisher.publishEvent(AuthEvent(user, servletRequest.contextPath))
    }

    private fun validateNewUser(email: String){
        check(userService.isUserDoesNotExist(email)){
            throw EntityExistsException("User with email $email already exists")
        }
    }

    private fun doPasswordsMatch(p1: String, p2: String){
        check(p1 == p2){
            throw PasswordsDoNotMatchException("Passwords do not match")
        }
    }

    private fun encodePassword(password: String): String{
        return passwordEncoder.encode(password)
    }

    private fun getRoleName(roleName: String): Role{
        check(isValidRole(roleName)){
            throw IllegalStateException("Invalid role name")
        }
        return Role.valueOf(roleName.uppercase())
    }

    private fun isValidRole(roleName: String): Boolean {
        return Arrays.stream(Role.values()).anyMatch { role -> role.name.equals(roleName, ignoreCase = true) }
    }
}