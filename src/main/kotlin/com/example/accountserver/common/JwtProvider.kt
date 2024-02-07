package com.example.accountserver.common

import com.example.accountserver.domain.token.RefreshToken
import com.example.accountserver.domain.token.RefreshTokenRepository
import com.example.accountserver.domain.user.User
import com.example.accountserver.error.UserInactiveException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Component
class JwtProvider (
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.access.private-key}") private val accessPrivateKey: String,
    @Value("\${auth.jwt.refresh.public-key}") private val refreshPublicKey: String,
    @Value("\${auth.jwt.refresh.private-key}") private val refreshPrivateKey: String,
) {
    private final val decoder: Base64.Decoder = Base64.getDecoder()
    private final val factory: KeyFactory = KeyFactory.getInstance("RSA")

    private final val accessPrivateKeyEncoded = PKCS8EncodedKeySpec(decoder.decode(accessPrivateKey))
    private final val refreshPublicKeyEncoded = X509EncodedKeySpec(decoder.decode(refreshPublicKey))
    private final val refreshPrivateKeyEncoded = PKCS8EncodedKeySpec(decoder.decode(refreshPrivateKey))

    private final val accessPrivateKeyGenerated: PrivateKey = factory.generatePrivate(accessPrivateKeyEncoded)
    private final val refreshPublicKeyGenerated: PublicKey = factory.generatePublic(refreshPublicKeyEncoded)
    private final val refreshPrivateKeyGenerated: PrivateKey = factory.generatePrivate(refreshPrivateKeyEncoded)


    fun createAccessToken(user: User, now: LocalDateTime): String {
        val expiration = now.plusDays(1)

        return this.buildJwtToken(user, accessPrivateKeyGenerated, now, expiration)
    }

    suspend fun createRefreshToken(user: User, now: LocalDateTime): String {
        val expiration = now.plusDays(365)
        val refreshToken = this.buildJwtToken(user, refreshPrivateKeyGenerated, now, expiration)

        refreshTokenRepository.save(
            refreshTokenRepository.findByUserId(user.id!!)?.apply {
                token = refreshToken
                tokenHash = refreshToken.sha256()
                expireAt = expiration
            } ?: RefreshToken(
                userId = user.id!!,
                token = refreshToken,
                tokenHash = refreshToken.sha256(),
                expireAt = expiration
            )
        )

        return refreshToken
    }

    private fun buildJwtToken(
        user: User,
        key: PrivateKey,
        issuedAt: LocalDateTime,
        expiration: LocalDateTime
    ): String {
        if (!user.isActive) throw UserInactiveException

        return Jwts.builder()
            .setIssuer(issuer)
            .setSubject(user.id!!.toString())
            .setIssuedAt(Timestamp.valueOf(issuedAt))
            .setExpiration(Timestamp.valueOf(expiration))
            .signWith(key, SignatureAlgorithm.RS512)
            .compact()
    }
}