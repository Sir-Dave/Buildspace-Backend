package com.sirdave.buildspace.subscription_plan

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PlanConfig {

    @Bean
    fun commandLineRunner(repository: SubscriptionPlanRepository): CommandLineRunner {
        return CommandLineRunner {
            val daily = SubscriptionPlan(
                name = "Daily",
                amount = 1500.0,
                numberOfDays = 1,
                type = SubscriptionType.INDIVIDUAL
            )

            val weekly = SubscriptionPlan(
                name = "Weekly",
                amount = 5000.0,
                numberOfDays = 7,
                type = SubscriptionType.INDIVIDUAL
            )

            val monthly = SubscriptionPlan(
                name = "Monthly",
                amount = 10000.0,
                numberOfDays = 28,
                type = SubscriptionType.INDIVIDUAL
            )

            val teamMonthly = SubscriptionPlan(
                name = "Monthly (Team)",
                amount = 40000.0,
                numberOfDays = 30,
                type = SubscriptionType.TEAM
            )

            val quarterly = SubscriptionPlan(
                name = "Quarterly (Team)",
                amount = 114000.0,
                numberOfDays = 90,
                type = SubscriptionType.TEAM
            )
            val biannual = SubscriptionPlan(
                name = "Biannual (Team)",
                amount = 216000.0,
                numberOfDays = 180,
                type = SubscriptionType.TEAM
            )
            val yearly = SubscriptionPlan(
                name = "Yearly (Team)",
                amount = 408000.0,
                numberOfDays = 365,
                type = SubscriptionType.TEAM
            )
            repository.save(daily)
            repository.save(weekly)
            repository.save(monthly)
            repository.save(teamMonthly)
            repository.save(quarterly)
            repository.save(biannual)
            repository.save(yearly)
        }
    }
}