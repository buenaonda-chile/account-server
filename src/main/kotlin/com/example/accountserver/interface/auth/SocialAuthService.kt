package com.example.accountserver.`interface`.auth

import com.example.accountserver.common.JwtProvider
import com.example.accountserver.domain.enum.SocialProvider
import com.example.accountserver.domain.user.User
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.EmailAlreadyExistsException
import com.example.accountserver.error.SocialConnectFailException
import com.example.accountserver.social.OAuth2Client
import com.example.accountserver.social.OAuth2Response
import org.springframework.stereotype.Service

@Service
class SocialAuthService(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    clients: Map<String, OAuth2Client>
) {
    private val clients = clients.mapKeys { SocialProvider.valueOf(it.key) }

    suspend fun socialLoginWithAuthCode(
        socialProvider: SocialProvider,
        oAuth2Request: OAuth2RequestWithAuthCode
    ): TokenResponse {
        val oAuth2Client = clients[socialProvider]!!

        val oAuth2Response = oAuth2Client.getMeWithAuthCode(oAuth2Request) ?: throw SocialConnectFailException

        return socialSignUpOrLogin(socialProvider, oAuth2Response)
    }

    private suspend fun socialSignUpOrLogin(
        socialProvider: SocialProvider,
        oAuth2Response: OAuth2Response
    ): TokenResponse {
        val user = userRepository.findByProviderAndSocialId(socialProvider, oAuth2Response.socialId)
            ?: socialSignUp(socialProvider, oAuth2Response)

        val accessToken = jwtProvider.createJwtToken(user)
        val refreshToken = jwtProvider.createJwtToken(user)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private suspend fun socialSignUp(
        socialProvider: SocialProvider,
        oAuth2Response: OAuth2Response
    ): User {
        val email = oAuth2Response.email
            ?.let { checkDuplicatedSignup(it) }

        return userRepository.save(
            User(
                provider = socialProvider,
                socialId = oAuth2Response.socialId,
                email = email,
                password = null,
                username = null
            )
        )
    }

    private suspend fun checkDuplicatedSignup(email: String) : String{
        val user = userRepository.findByEmail(email) ?: return email

        throw EmailAlreadyExistsException
    }
}