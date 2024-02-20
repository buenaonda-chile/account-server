package com.example.accountserver.domain.verification

import com.example.accountserver.domain.enum.VerificationMethod
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationCodeRepository: CoroutineCrudRepository<VerificationCode, Long> {
    suspend fun findByTargetAndIsValid(target: String, isValid: Boolean): VerificationCode?
    suspend fun findByCodeAndMethodAndUserId(code: String, method: VerificationMethod, userId: Long): VerificationCode?
}