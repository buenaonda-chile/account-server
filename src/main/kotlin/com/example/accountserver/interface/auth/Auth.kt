package com.example.accountserver.`interface`.auth

import org.jetbrains.annotations.NotNull

class Auth {
    data class SignUpRequest(
        val email: String,
        val password: String
    )

    data class SignUpResponse(
        val accessToken: String,
        val refreshToken: String
    )
}