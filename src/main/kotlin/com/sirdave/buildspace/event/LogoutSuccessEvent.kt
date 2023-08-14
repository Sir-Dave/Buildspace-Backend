package com.sirdave.buildspace.event

data class LogoutSuccessEvent(
    val userEmail: String,
    val token: String
)