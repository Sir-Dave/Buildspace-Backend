package com.sirdave.buildspace.user

import com.sirdave.buildspace.mapper.toUserDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(path = ["api/v1/users"])
class UserController(private val userService: UserService) {

    @Operation(summary = "Find user by id")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: String): ResponseEntity<UserDto> {
        val user = userService.findUserById(UUID.fromString(id)).toUserDto()
        return ResponseEntity(user, HttpStatus.OK)
    }

    @Operation(summary = "Update user info")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable("id") id: String,
                   @RequestParam firstName: String?,
                   @RequestParam lastName: String?,
                   @RequestParam phoneNumber: String?
    ): ResponseEntity<UserDto> {
        val user = userService.updateUser(
            id = UUID.fromString(id),
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber
        ).toUserDto()
        return ResponseEntity(user, HttpStatus.OK)
    }

    @Operation(summary = "Get all users. Endpoint accessible only to users with admin roles")
    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") pageSize: Int,
    ): ResponseEntity<List<UserDto>>{
        val users = userService.getAllUsers(page, pageSize)
        return ResponseEntity(users, HttpStatus.OK)
    }

}