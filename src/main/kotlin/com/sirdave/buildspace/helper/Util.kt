package com.sirdave.buildspace.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun formatDate(date: String): LocalDateTime {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return LocalDateTime.parse(date, formatter)
}

fun getSubscriptionType(name: String): SubscriptionType {
    check(isValidSubscriptionType(name)){
        throw IllegalStateException("Invalid subscription type")
    }
    return SubscriptionType.valueOf(name.uppercase())
}

fun isValidSubscriptionType(name: String): Boolean {
    return Arrays.stream(SubscriptionType.values()).anyMatch { type -> type.name.equals(name, ignoreCase = true) }
}