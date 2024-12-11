package dev.bittim.valolink.user.data.validator

interface EmailPatternValidator {
    fun isValid(email: String): Boolean
}