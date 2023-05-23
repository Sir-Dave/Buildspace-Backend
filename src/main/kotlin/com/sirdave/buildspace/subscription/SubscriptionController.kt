package com.sirdave.buildspace.subscription

import com.sirdave.buildspace.helper.ApiResponse
import com.sirdave.buildspace.mapper.toSubscriptionDto
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping(path = ["/api/v1/subscriptions"])
class SubscriptionController(private val subscriptionService: SubscriptionService) {

    @Operation(summary = "Create a new subscription")
    @PostMapping
    fun createNewSubscription(
        @RequestBody subscriptionRequest: SubscriptionRequest): ResponseEntity<ApiResponse>{
        subscriptionService.createSubscription(subscriptionRequest)
        val response = ApiResponse(
            HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.reasonPhrase,
            "Created subscription successfully"
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

    @Operation(summary = "Get all subscriptions by a user")
    @GetMapping
    fun getAllSubscriptionsByUser(@RequestParam userId: String): ResponseEntity<Set<SubscriptionDto>>{
        val subscription = subscriptionService.findAllByUser(UUID.fromString(userId))
        return ResponseEntity(subscription, HttpStatus.OK)
    }


    @Operation(summary = "Get one subscription by its id")
    @GetMapping("/{id}")
    fun getSubscriptionById(@PathVariable("id") id: String): ResponseEntity<SubscriptionDto>{
        val subscription = subscriptionService.findById(UUID.fromString(id)).toSubscriptionDto()
        return ResponseEntity(subscription, HttpStatus.OK)
    }

    @Operation(summary = "Get user current subscription")
    @GetMapping("/current")
    fun getCurrentUserSubscription(@RequestParam userId: String): ResponseEntity<SubscriptionDto>{
        val subscription = subscriptionService.getUserCurrentSubscription(UUID.fromString(userId))
        return ResponseEntity(subscription, HttpStatus.OK)
    }

}