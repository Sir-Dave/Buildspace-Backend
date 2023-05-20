package com.sirdave.buildspace.auth

import javax.servlet.http.HttpServletRequest

interface AuthService {
    fun register(registerRequest: RegisterRequest, servletRequest: HttpServletRequest)
}