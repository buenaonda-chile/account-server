package com.example.accountserver.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("account_user")
class User (
    @Id
    var id: Long? = null,

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
) {
    constructor(email: String, password: String) : this(null, null, email, password, null)
}