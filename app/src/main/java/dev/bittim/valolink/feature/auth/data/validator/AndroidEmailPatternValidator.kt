package dev.bittim.valolink.feature.auth.data.validator

import android.util.Patterns

class AndroidEmailPatternValidator : EmailPatternValidator {
    override fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}