/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   22.04.25, 20:26
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.graphics.Bitmap
import dev.bittim.valolink.core.ui.util.UiText

data class ProfileSetupState(
    val isAvatarCustom: Boolean = false,

    val isAuthenticated: Boolean? = null,
    val isLocal: Boolean? = null,

    val username: String = "",
    val private: Boolean = false,
    val avatar: Bitmap? = null,

    val imageError: UiText? = null,
    val usernameError: UiText? = null,
    val avatarError: UiText? = null,
)
