package com.example.accountserver.domain.user

import com.example.accountserver.domain.enum.SocialProvider
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository:CoroutineCrudRepository<User, Long> {
    suspend fun findByEmail(email: String): User?
    suspend fun findByProviderAndSocialId(provider: SocialProvider, socialId: String): User?
    suspend fun findAllByIdIsIn(userIds: List<Long>): List<User>
}