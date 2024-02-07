package com.example.accountserver.`interface`.auth

import com.example.accountserver.domain.enum.SocialProvider
import org.springframework.expression.spel.ast.TypeCode
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val emailAuthService: EmailAuthService,
    private val socialAuthService: SocialAuthService
) {
    @PostMapping("/v1/users")
    suspend fun signUp(
        @RequestBody request: AuthRequest
    ): TokenResponse {
        return emailAuthService.emailSignUp(request)
    }

    @PostMapping
    suspend fun socialLoginWithAuthCode(
        @PathVariable socialProvider: SocialProvider,
        @RequestBody oAuth2RequestWithCode: OAuth2RequestWithAuthCode
    ): TokenResponse {
        return socialAuthService.socialLoginWithAuthCode(socialProvider, oAuth2RequestWithCode)
    }

    @PostMapping
    suspend fun socialLoginWithAccessToken(
        @PathVariable socialProvider: SocialProvider,
        @RequestBody oAuth2RequestWithAccessToken: OAuth2RequestWithAccessToken
    ): TokenResponse {
        return socialAuthService.
    }
}