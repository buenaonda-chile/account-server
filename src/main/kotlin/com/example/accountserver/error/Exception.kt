package com.example.accountserver.error

import javax.security.auth.login.AccountException

open class Exception(val errorType: ErrorType): RuntimeException(errorType.name)

object EmailAlreadyExistsException: Exception(ErrorType.EMAIL_ALREADY_EXISTS)