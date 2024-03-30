package dev.bittim.valolink.feature.auth.data.validator

interface EmailPatternValidator {
    fun isValid(email: String): Boolean
}