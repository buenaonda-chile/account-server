package com.example.accountserver.domain.token

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("account_refresh_token")
class RefreshToken (
    @Id
    var id :Long? = null,

    var userId: Long,

    var token: String,

    var tokenHash: String,

    val usedTokenId: Long? = null,

    var expireAt: LocalDateTime,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),
)