package com.sirdave.buildspace.helper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


fun formatDate(date: String): LocalDateTime {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return LocalDateTime.parse(date, formatter)
}

inline fun <reified T : Enum<T>> getEnumName(name: String): T {
    check (isValidEnum<T>(name)){
        throw IllegalStateException("Invalid ${T::class.simpleName} name")
    }
    return enumValueOf(name.uppercase())
}

inline fun <reified T : Enum<T>> isValidEnum(name: String): Boolean {
    return enumValues<T>().any { enum -> enum.name.equals(name, ignoreCase = true) }
}