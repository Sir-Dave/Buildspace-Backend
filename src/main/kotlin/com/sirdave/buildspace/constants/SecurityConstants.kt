package com.sirdave.buildspace.constants

object SecurityConstants {
    const val EXPIRATION_DATE = 604_800_000 // 7 days
    const val TOKEN_PREFIX = "Bearer "
    const val JWT_TOKEN_HEADER = "Jwt-Token"
    const val JWT_ISSUER = "BuildSpace Company"
    const val JWT_AUDIENCE = "BuildSpace Backend Application"
    const val AUTHORITIES = "authorities"
    const val OPTIONS_HTTP_METHOD = "OPTIONS"
    const val TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified"
    const val FORBIDDEN_MESSAGE = "You need to log in to access this page"
    const val ACCESS_DENIED = "You do not have permission to access this page"
    private const val PAYMENT_WEBHOOK = "/api/v1/payments/webhook"
    val PUBLIC_URLS = arrayOf("/api/v1/auth/**", PAYMENT_WEBHOOK)
}