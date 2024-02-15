package com.example.accountserver.`interface`.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshResponse(
    @JsonProperty("access_token")
    val accessToken: String,
)

data class UserResponse(
    @JsonProperty("user_id")
    val userId: Long,

    val username: String?,

    val email: String?,

    @JsonProperty("is_active")
    val isActive: Boolean,

    @JsonProperty("is_banned")
    val isBanned: Boolean,

    val provider: String
)

data class UserIDResponse(
    @JsonProperty("user_id")
    val userId: Long,
)

data class UnregisterResponse(
    val unregistered: Boolean
)
