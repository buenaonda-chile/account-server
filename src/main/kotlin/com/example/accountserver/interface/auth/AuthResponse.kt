package com.example.accountserver.`interface`.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
