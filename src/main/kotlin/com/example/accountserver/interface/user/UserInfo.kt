package com.example.accountserver.`interface`.user

import com.example.accountserver.domain.enum.SocialProvider
import com.fasterxml.jackson.annotation.JsonProperty

data class UserInfo(
    val username: String? = null,
    val email: String? = null,
    @JsonProperty("is_active")
    val isActive: Boolean = true,
    @JsonProperty("is_active")
    val isBanned: Boolean = false,
    @JsonProperty("created_at")
    val createdAt: String,
    @JsonProperty("updated_at")
    val updatedAt: String,
    val provider: SocialProvider = SocialProvider.LOCAL,
    @JsonProperty("social_id")
    val socialId: String? = null
)

data class UserInfoRequest(
    val username: String?,
    val isActive: Boolean?,
    val isBanned: Boolean?,
)

data class UserInfosResponse(
    val userInfos: List<UserInfo>
)

