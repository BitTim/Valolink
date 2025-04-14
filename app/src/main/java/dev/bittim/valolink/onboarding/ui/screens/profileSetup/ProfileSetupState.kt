/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.04.25, 02:40
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.graphics.Bitmap
import dev.bittim.valolink.core.ui.util.UiText

data class ProfileSetupState(
    val loading: Boolean = false,
    val isLocal: Boolean = false,
    val isAvatarCustom: Boolean = false,

    val imageError: UiText? = null,
    val usernameError: UiText? = null,
    val avatar: Bitmap? = null,
    val avatarError: UiText? = null,
)
