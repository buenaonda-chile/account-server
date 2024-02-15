package com.example.accountserver.error

import org.springframework.http.HttpStatus

enum class ErrorType(
    val code: Int,
    val httpStatus: HttpStatus
) {

    // 401
    INVALID_TOKEN(1401001, HttpStatus.UNAUTHORIZED),
    WRONG_PASSWORD(1401002, HttpStatus.UNAUTHORIZED),
    SOCIAL_CONNECT_FAIL(1401003, HttpStatus.UNAUTHORIZED),

    //403
    USER_INACTIVE(1234, HttpStatus.FORBIDDEN),

    //409
    EMAIL_ALREADY_EXISTS(12345, HttpStatus.CONFLICT),

    // 404
    USER_NOT_FOUND(1404001, HttpStatus.NOT_FOUND),


}