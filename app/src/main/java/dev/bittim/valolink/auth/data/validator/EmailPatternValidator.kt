package dev.bittim.valolink.auth.data.validator

interface EmailPatternValidator {
    fun isValid(email: String): Boolean
}