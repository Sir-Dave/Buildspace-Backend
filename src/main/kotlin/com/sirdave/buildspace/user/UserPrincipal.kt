package com.sirdave.buildspace.user

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Arrays.stream
import java.util.stream.Collectors

class UserPrincipal(private var user: User?): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return stream(user!!.authorities).map { role ->
            SimpleGrantedAuthority(role)
        }.collect(Collectors.toList())
    }

    override fun getPassword(): String {
        return this.user!!.password!!
    }

    override fun getUsername(): String {
        return this.user!!.email!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return this.user!!.isNotLocked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return this.user!!.isActive
    }

}
