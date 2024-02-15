package com.example.accountserver.domain.verification

import com.example.accountserver.domain.enum.VerificationMethod
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("varification_code")
class VerificationCode (
    @Id
    var id: Long? = null,

    val code: String,

    val target: String,

    val sentAt: LocalDateTime,

    var verifiedAt: LocalDateTime? = null,

    val expireAt: LocalDateTime,

    val method: VerificationMethod,

    val userId: Long,

    var isValid: Boolean,
) {

}