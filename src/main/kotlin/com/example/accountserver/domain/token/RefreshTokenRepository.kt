package com.example.accountserver.domain.token

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: CoroutineCrudRepository<RefreshToken, Long> {
    suspend fun findByUserId(userId: Long): RefreshToken?
}