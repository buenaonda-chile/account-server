package com.example.accountserver.domain.user

import com.example.accountserver.domain.enum.SocialProvider
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("account_user")
class User (
    @Id
    var id: Long? = null,

    var provider: SocialProvider,

    val socialId: String?,

    val email: String?,

    var password: String?,

    var username: String?,

    var phone: String? = null,

    var verifiedEmail: String? = null,

    var verifiedSnuEmail: String? = null,

    var isActive: Boolean = true,

    var isBanned: Boolean = false,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now(),
)