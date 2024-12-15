/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       SignUpState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.auth.ui.screens.signup

data class SignUpState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val emailError: String? = null,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val authError: String? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)
