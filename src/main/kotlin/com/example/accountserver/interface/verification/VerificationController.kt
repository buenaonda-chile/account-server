package com.example.accountserver.`interface`.verification

import com.example.accountserver.domain.enum.VerificationMethod
import com.example.accountserver.domain.verification.VerificationCode
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.AssertingParty.Verification
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/verification")
class VerificationController(
    private val verificationService: VerificationService
) {
    @PostMapping("/send/{method}")
    suspend fun sendVerificationCode(
        @RequestHeader("user-id") userId: Long,
        @RequestBody request: VerificationSendRequest,
        @PathVariable method: VerificationMethod
    ){
        verificationService.sendVerificationCode(userId, request, method)
    }

    @PostMapping("/check/{method}")
    suspend fun checkVerification(
        @RequestHeader("user-id") userId: Long,
        @RequestBody request: VerificationCheckRequest,
        @PathVariable method: VerificationMethod
    ) {
        verificationService.checkVerificationCode(userId, request, method)
    }
}