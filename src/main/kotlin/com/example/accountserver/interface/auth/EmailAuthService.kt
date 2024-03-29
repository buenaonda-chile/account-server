package com.example.accountserver.`interface`.auth

import com.example.accountserver.common.JwtProvider
import com.example.accountserver.common.sha256
import com.example.accountserver.domain.enum.SocialProvider
import com.example.accountserver.domain.token.RefreshToken
import com.example.accountserver.domain.user.User
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.EmailAlreadyExistsException
import com.example.accountserver.error.UserDoesNotExistsException
import com.example.accountserver.error.WrongPasswordException
import org.springframework.cglib.core.Local
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils
import java.time.LocalDateTime

@Service
class EmailAuthService (
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    suspend fun emailSignUp(request: AuthRequest): TokenResponse {
        if (!ObjectUtils.isEmpty(userRepository.findByEmail(request.email))) {
            throw EmailAlreadyExistsException
        }

        val user = userRepository.save(
            User(
                provider = SocialProvider.LOCAL,
                socialId = null,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                username = null
            )
        )

        val now = LocalDateTime.now()
        val accessToken = jwtProvider.createAccessToken(user, now)
        val refreshToken = jwtProvider.createRefreshToken(user, now)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    suspend fun emailLogin(request: AuthRequest): TokenResponse {
        val user = userRepository.findByEmail(request.email) ?: throw UserDoesNotExistsException

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw WrongPasswordException
        }

        val now = LocalDateTime.now()
        val accessToken = jwtProvider.createAccessToken(user, now)
        val refreshToken = jwtProvider.createRefreshToken(user, now)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}