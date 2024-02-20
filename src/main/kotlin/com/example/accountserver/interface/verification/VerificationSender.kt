package com.example.accountserver.`interface`.verification

import com.example.accountserver.domain.user.User

interface VerificationSender {
    suspend fun sendCode(target: String, code: String)
    suspend fun changeUserInfo(user: User, target: String): User
    suspend fun checkTarget(target: String): Boolean
}