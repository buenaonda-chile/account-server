package com.example.accountserver.`interface`.verification

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient
import com.amazonaws.services.simpleemail.model.*
import com.example.accountserver.domain.user.User
import org.springframework.stereotype.Component

@Component
class EmailSender: VerificationSender {

    private val regex = Regex("^[-_.0-9a-zA-Z]+@[-_.0-9a-zA-Z]+\$")

    override suspend fun sendCode(target: String, code: String) {
        val client = AmazonSimpleEmailServiceClient.builder().withRegion("ap-northeast-2").build()
        client.sendEmail(
            SendEmailRequest(
                "qwgh123@naver.com",
                Destination(listOf(target)),
                Message(
                    Content("SSO Verification Code"),
                    Body(Content(code))
                )
            )
        )
    }

    override suspend fun changeUserInfo(user: User, target: String): User {
        user.verifiedEmail = target
        if (target.endsWith("@snu.ac.kr")) user.verifiedSnuEmail = target
        return user
    }

    override suspend fun checkTarget(target: String): Boolean {
        return regex.matches(target)
    }
}