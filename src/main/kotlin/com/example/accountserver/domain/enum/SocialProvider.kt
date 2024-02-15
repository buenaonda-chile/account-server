package com.example.accountserver.domain.enum

enum class SocialProvider(val value: String) {
    LOCAL("local"),
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao"),
    GITHUB("github"),
    APPLE("apple");

    companion object {
        private val mapping = entries.associateBy(SocialProvider::value)
        fun customValueOf(value: String): SocialProvider? = mapping[value]
    }
}