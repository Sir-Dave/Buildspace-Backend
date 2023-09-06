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

    @Operation(summary = "Show a list of available subscription plans. Filter by individual or teams.")
    @GetMapping("/plans")
    fun getAllSubscriptionPlans(
        @RequestParam("category") category: String
    ): ResponseEntity<List<SubscriptionPlanDto>>{
        val plans = subscriptionService.getAllSubscriptionPlans(category)
        return ResponseEntity(plans, HttpStatus.OK)
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

    @Operation(summary = "Remove subscription")
    @PostMapping("/remove-subscriptions")
    fun removeExpiredSubscriptions(): ResponseEntity<ApiResponse>{
        subscriptionService.setExpiredFields()
        val response = ApiResponse(
            HttpStatus.OK.value(),
            HttpStatus.OK,
            HttpStatus.OK.reasonPhrase,
            "Removed expired subscriptions successfully."
        )
        return ResponseEntity(response, HttpStatus.OK)
    }

}