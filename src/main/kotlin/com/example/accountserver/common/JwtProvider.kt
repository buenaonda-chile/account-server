package com.example.accountserver.common

import com.example.accountserver.domain.user.User
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class JwtProvider {
    fun createJwtToken(user: User): String {
        val now = LocalDateTime.now()
        val accessTokenExpire = now.plusDays(1)
        val refreshTokenExpire = now.plusDays(365)

        return Jwts.builder()

            .compact()
    }
}