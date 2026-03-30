/*
 Copyright (c) 2025-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   30.03.26, 03:06
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.graphics.Bitmap
import dev.bittim.valolink.core.ui.util.UiText

data class ProfileSetupState(
    val isAvatarCustom: Boolean = false,
    val isAuthenticated: Boolean? = null,

    val username: String = "",
    val private: Boolean = false,
    val avatar: Bitmap? = null,

    val imageError: UiText? = null,
    val usernameError: UiText? = null,
    val avatarError: UiText? = null,
)
