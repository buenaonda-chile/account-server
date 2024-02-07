package com.example.accountserver.`interface`.auth

import com.example.accountserver.common.JwtProvider
import com.example.accountserver.common.sha256
import com.example.accountserver.domain.enum.SocialProvider
import com.example.accountserver.domain.token.RefreshToken
import com.example.accountserver.domain.token.RefreshTokenRepository
import com.example.accountserver.domain.user.User
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.error.EmailAlreadyExistsException
import com.example.accountserver.error.UserDoesNotExistsException
import com.example.accountserver.error.WrongPasswordException
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

}
