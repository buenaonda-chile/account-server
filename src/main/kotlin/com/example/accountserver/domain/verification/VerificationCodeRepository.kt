package com.example.accountserver.domain.verification

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationCodeRepository: CoroutineCrudRepository<VerificationCode, Long> {
}