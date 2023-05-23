package com.sirdave.buildspace.user

import com.sirdave.buildspace.mapper.toUserDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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

    @Operation(summary = "Get all users. Endpoint accessible only to users with admin roles")
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDto>>{
        val users = userService.getAllUsers()
        return ResponseEntity(users, HttpStatus.OK)
    }

}