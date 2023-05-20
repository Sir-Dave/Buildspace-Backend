package com.sirdave.buildspace.event

import com.sirdave.buildspace.user.User

class AuthEvent(val user: User, val appUrl: String)