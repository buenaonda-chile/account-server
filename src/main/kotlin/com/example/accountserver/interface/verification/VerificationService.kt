package com.example.accountserver.`interface`.verification

import com.example.accountserver.domain.enum.VerificationMethod
import com.example.accountserver.domain.user.UserRepository
import com.example.accountserver.domain.verification.VerificationCode
import com.example.accountserver.domain.verification.VerificationCodeRepository
import com.example.accountserver.error.UserDoesNotExistsException
import com.example.accountserver.error.VerificationCodeDoesNotExistsException
import com.example.accountserver.error.VerificationCodeExpiredException
import com.example.accountserver.error.VerificationTargetInvalidException
import io.netty.util.internal.ThreadLocalRandom
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class VerificationService(
    private val userRepository: UserRepository,
    private val verificationCodeRepository: VerificationCodeRepository,
    private val emailSender: EmailSender,
    private val smsSender: SMSSender
) {
    private val senders: Map<VerificationMethod, VerificationSender> = mapOf(
        VerificationMethod.SMS to smsSender,
        VerificationMethod.EMAIL to emailSender
    )

    suspend fun sendVerificationCode(
        userId: Long,
        request: VerificationSendRequest,
        verificationMethod: VerificationMethod
    ) {
        val sender = senders[verificationMethod]!!
        if (!sender.checkTarget(request.target)) throw VerificationTargetInvalidException
        if (!userRepository.existsById(userId)) throw UserDoesNotExistsException

        verificationCodeRepository.findByTargetAndIsValid(request.target, true)?.let {
            it.isValid = false
            verificationCodeRepository.save(it)
        }

        val code = ThreadLocalRandom.current().nextLong(100000, 1000000).toString()
        sender.sendCode(request.target, code)

        val now = LocalDateTime.now();
        verificationCodeRepository.save(
            VerificationCode(
                code = code,
                target = request.target,
                sentAt = now,
                expireAt = now.plusMinutes(3),
                method = verificationMethod,
                userId = userId,
                isValid = true
            )
        )
    }

    suspend fun checkVerificationCode(
        userId: Long,
        verificationCheckRequest: VerificationCheckRequest,
        verificationMethod: VerificationMethod,
    ) {
        val sender = senders[verificationMethod]!!
        val user = userRepository.findById(userId) ?: throw UserDoesNotExistsException
        val verificationCode = verificationCodeRepository.findByCodeAndMethodAndUserId(
            verificationCheckRequest.code, verificationMethod, userId
        ) ?: throw VerificationCodeDoesNotExistsException

        if (!verificationCode.isValid) throw VerificationCodeExpiredException
        verificationCode.isValid = false
        if (verificationCode.expireAt.isBefore(LocalDateTime.now())) {
            verificationCodeRepository.save(verificationCode)
            throw VerificationCodeExpiredException
        } else {
            verificationCode.verifiedAt = LocalDateTime.now()
            verificationCodeRepository.save(verificationCode)
        }

        val modifiedUser = sender.changeUserInfo(user, verificationCode.target)
        modifiedUser.updatedAt = LocalDateTime.now()
        userRepository.save(modifiedUser)
    }
}