package com.example.accountserver.`interface`.auth

import com.example.accountserver.domain.enum.SocialProvider
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val emailAuthService: EmailAuthService,
    private val socialAuthService: SocialAuthService
) {
    @PostMapping("/sign-up")
    suspend fun signUp(
        @RequestBody request: AuthRequest
    ): TokenResponse {
        return emailAuthService.emailSignUp(request)
    }

    @PostMapping
    suspend fun login(
        @RequestBody request: AuthRequest
    ): TokenResponse {
        return emailAuthService.emailLogin(request)
    }

    @DeleteMapping
    suspend fun unRegister(
        @RequestHeader("user-id") userId: Long
    ): UnregisterResponse {
        return authService.unRegister(userId)
    }

    @PostMapping("/login/{socialProvider}/code")
    suspend fun socialLoginWithAuthCode(
        @PathVariable socialProvider: SocialProvider,
        @RequestBody oAuth2RequestWithCode: OAuth2RequestWithAuthCode
    ): TokenResponse {
        return socialAuthService.socialLoginWithAuthCode(socialProvider, oAuth2RequestWithCode)
    }

    @PostMapping("/login/{socialProvider}/token")
    suspend fun socialLoginWithAccessToken(
        @PathVariable socialProvider: SocialProvider,
        @RequestBody oAuth2RequestWithAccessToken: OAuth2RequestWithAccessToken
    ): TokenResponse {
        return socialAuthService.socialLoginWithAccessToken(socialProvider, oAuth2RequestWithAccessToken)
    }

    @GetMapping("/info")
    suspend fun getUserInformation(
        @RequestHeader("user-id") userId: Long
    ): UserResponse {
        return authService.getUser(UserIDRequest(userId = userId))
    }

    @PutMapping("/token/validate")
    suspend fun tokenValidate(
        @RequestHeader("user-id") userId: Long,
    ): UserIDResponse {
        return UserIDResponse(
            userId = userId,
        )
    }

    @PutMapping("/token/refresh")
    suspend fun tokenRefresh(
        @RequestBody refreshRequest: RefreshRequest,
    ): RefreshResponse {
        return authService.refresh(refreshRequest)
    }
}