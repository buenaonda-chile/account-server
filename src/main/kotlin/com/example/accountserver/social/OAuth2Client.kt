package com.example.accountserver.social

import com.example.accountserver.`interface`.auth.OAuth2RequestWithAuthCode

interface OAuth2Client {
    suspend fun getMeWithAuthCode(oAuth2RequestWithAuthCode: OAuth2RequestWithAuthCode): OAuth2Response?
    suspend fun getMe(token: String): OAuth2Response?
}