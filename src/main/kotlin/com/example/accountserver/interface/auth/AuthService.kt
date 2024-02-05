package com.example.accountserver.`interface`.auth

import com.example.accountserver.common.JwtProvider
import com.example.accountserver.common.sha256
import com.example.accountserver.domain.token.RefreshToken
import com.example.accountserver.domain.token.RefreshTokenRepository
import com.example.accountserver.domain.user.User
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.EmailAlreadyExistsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.ObjectUtils

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {
    suspend fun signUp(request: Auth.SignUpRequest): Auth.SignUpResponse {
        if (!ObjectUtils.isEmpty(userRepository.findByEmail(request.email))) {
            throw EmailAlreadyExistsException
        }

        val user = userRepository.save(
            User(
                email = request.email,
                password = passwordEncoder.encode(request.password)
            )
        )

        val accessToken = jwtProvider.createJwtToken(user)
        val refreshToken = jwtProvider.createJwtToken(user)

        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id!!,
                token = refreshToken,
                tokenHash = refreshToken.sha256()
            )
        )

        return Auth.SignUpResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }
}
