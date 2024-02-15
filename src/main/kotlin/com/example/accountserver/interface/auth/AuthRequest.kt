package com.example.accountserver.`interface`.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthRequest(
    val email: String,
    val password: String
)

data class RefreshRequest(
    @JsonProperty("refresh_token")
    val refreshToken: String,
)

data class UserIDRequest(
    val userId: Long,
)

data class OAuth2RequestWithAccessToken(
    @JsonProperty("access_token")
    val accessToken: String,
)

data class OAuth2RequestWithAuthCode(
    @JsonProperty("authorization_code")
    val authorizationCode: String,
    @JsonProperty("redirect_uri")
    val redirectUri: String,
)