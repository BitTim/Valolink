package dev.bittim.valolink.feature.auth.util

import android.util.Patterns

object AuthValidator {
    fun validateEmail(email: String): String? {
        return if (email.isEmpty()) {
            "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email"
        } else {
            null
        }
    }

    fun validateUsername(username: String): String? {
        return if (username.isEmpty()) {
            "Username is required"
        } else if (username.length < 4) {
            "Username must be at least 4 characters"
        } else {
            null
        }
    }

    fun validatePassword(password: String): String? {
        return if (password.isEmpty()) {
            "Password is required"
        } else if (password.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return if (password != confirmPassword) {
            "Passwords do not match"
        } else {
            null
        }
    }
}