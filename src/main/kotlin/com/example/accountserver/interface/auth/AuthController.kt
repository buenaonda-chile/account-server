package com.example.accountserver.`interface`.auth

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/v1/users")
    suspend fun signUp(
        @RequestBody request: Auth.SignUpRequest
    ): Auth.SignUpResponse {
        return authService.signUp(request)
    }
}