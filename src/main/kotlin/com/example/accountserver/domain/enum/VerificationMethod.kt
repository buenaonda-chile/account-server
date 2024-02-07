package com.example.accountserver.domain.enum

enum class VerificationMethod(val value: String) {
    SMS("sms"),
    EMAIL("email");

    companion object {
        private val mapping = entries.associateBy(VerificationMethod::value)
        fun customValueOf(value: String): VerificationMethod? = mapping[value]
    }
}