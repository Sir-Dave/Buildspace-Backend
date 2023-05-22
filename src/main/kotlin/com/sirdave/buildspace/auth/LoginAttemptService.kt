package com.sirdave.buildspace.auth

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@Service
class LoginAttemptService{

    companion object{
        private const val MAXIMUM_LOGIN_ATTEMPT = 5
        private const val ATTEMPT_INCREMENT = 1
    }

    var loginAttemptCache: LoadingCache<String, Int> = CacheBuilder.newBuilder().
    expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(100)
        .build(object : CacheLoader<String, Int>(){
            override fun load(key: String): Int {
                return 0
            }
        })

    fun evictUserFromLoginAttemptCache(username: String){
        loginAttemptCache.invalidate(username)
    }

    fun addUserToLoginAttemptCache(username: String){
        var attempts = 0
        try {
            attempts = loginAttemptCache.get(username) + ATTEMPT_INCREMENT
        }
        catch (ex: ExecutionException){
            ex.printStackTrace()
        }
        loginAttemptCache.put(username, attempts)
    }

    fun hasExceededMaximumAttempt(username: String): Boolean{
        try {
            return loginAttemptCache.get(username) >= MAXIMUM_LOGIN_ATTEMPT
        }
        catch (ex: ExecutionException){
            ex.printStackTrace()
        }
        return false
    }
}