package com.example.accountserver.error

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val httpStatus: HttpStatus
) {
    //403
    USER_INACTIVE(1234, HttpStatus.FORBIDDEN),

    //409
    EMAIL_ALREADY_EXISTS(12345, HttpStatus.CONFLICT)
}