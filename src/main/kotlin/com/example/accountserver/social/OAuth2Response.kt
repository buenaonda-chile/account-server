package com.example.accountserver.social

import com.fasterxml.jackson.annotation.JsonProperty

data class OAuth2Response(
    val socialId: String,
    val email: String?,
)

data class GoogleOAuth2Response(
    val sub: String,
    val email: String,
)

data class NaverOAuth2Response(
    val response: NaverOAuth2UserInfo,
) {
    data class NaverOAuth2UserInfo(
        val id: String,
        val email: String,
    )
}

data class KakaoOAuth2Response(
    val id: Long,
    @JsonProperty("kakao_account")
    val kakaoAccount: KaKaoAccount,
)

data class KaKaoAccount(
    val email: String?,
    @JsonProperty("is_email_verified")
    val emailVerified: Boolean?,
)

data class GithubOAuth2Response(
    val id: Long,
    val email: String,
)

data class AppleOAuth2Response(
    val sub: String,
    val email: String,
)