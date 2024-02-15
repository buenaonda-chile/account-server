package com.example.accountserver.`interface`.auth

import com.example.accountserver.common.JwtProvider
import com.example.accountserver.domain.token.RefreshToken
import com.example.accountserver.domain.token.RefreshTokenRepository
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.TokenInvalidException
import com.example.accountserver.error.UserDoesNotExistsException
import com.example.accountserver.error.UserInactiveException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider
) {
    suspend fun getUser(request: UserIDRequest): UserResponse {
        val user = userRepository.findById(request.userId) ?: throw UserDoesNotExistsException

        return UserResponse(
            userId = request.userId,
            username = user.username,
            email = user.email,
            isActive = user.isActive,
            isBanned = user.isBanned,
            provider = user.provider.name
        )
    }

    suspend fun refresh(request: RefreshRequest): RefreshResponse {
        jwtProvider.validateToken(request.refreshToken)

        val refreshData: RefreshToken = refreshTokenRepository.findByToken(request.refreshToken) ?: throw TokenInvalidException

        val user = userRepository.findById(refreshData.userId) ?: throw UserDoesNotExistsException
        if (user.isActive) throw UserInactiveException

        val accessToken = jwtProvider.createAccessToken(user, LocalDateTime.now())

        return RefreshResponse(
            accessToken = accessToken
        )
    }

    suspend fun unRegister(userId: Long): UnregisterResponse {
        val user = userRepository.findById(userId) ?: throw UserDoesNotExistsException

        if (!user.isActive) return UnregisterResponse(unregistered = false)

        val now = LocalDateTime.now()
        user.isActive = false
        user.updatedAt = now
        userRepository.save(user)

        refreshTokenRepository.updateExpireAtByUserId(userId, now)

        return UnregisterResponse(unregistered = true)
    }
}
