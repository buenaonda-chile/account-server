package com.example.accountserver.domain.user

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository:CoroutineCrudRepository<User, Long> {
    fun findByEmail(email: String): User?
}