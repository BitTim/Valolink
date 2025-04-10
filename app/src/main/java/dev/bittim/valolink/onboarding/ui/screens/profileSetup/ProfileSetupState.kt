/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ProfileSetupState.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   11.04.25, 01:52
 */

package dev.bittim.valolink.onboarding.ui.screens.profileSetup

import android.graphics.Bitmap
import android.net.Uri
import dev.bittim.valolink.core.ui.util.UiText

data class ProfileSetupState(
    val loading: Boolean = false,

    val imageError: UiText? = null,
    val usernameError: UiText? = null,
    val generatedAvatar: Bitmap? = null,
    val selectedAvatar: Uri? = null,
)
