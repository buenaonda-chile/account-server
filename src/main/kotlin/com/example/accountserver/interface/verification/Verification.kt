package com.example.accountserver.`interface`.verification

data class VerificationSendRequest(
    val target: String
)

data class VerificationCheckRequest(
    val code: String
)