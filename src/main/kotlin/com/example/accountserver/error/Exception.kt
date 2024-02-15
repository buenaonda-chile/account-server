package com.example.accountserver.error


open class Exception(val errorType: ErrorType): RuntimeException(errorType.name)

object UserInactiveException: Exception(ErrorType.USER_INACTIVE)
object EmailAlreadyExistsException: Exception(ErrorType.EMAIL_ALREADY_EXISTS)
object UserDoesNotExistsException: Exception(ErrorType.USER_NOT_FOUND)
object WrongPasswordException: Exception(ErrorType.WRONG_PASSWORD)
object SocialConnectFailException: Exception(ErrorType.SOCIAL_CONNECT_FAIL)
object TokenInvalidException : Exception(ErrorType.INVALID_TOKEN)
