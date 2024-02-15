package com.example.accountserver.`interface`.verification

import org.springframework.web.bind.annotation.RestController

@RestController
class VerificationController(
    private val verificationService: VerificationService
) {

}