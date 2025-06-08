/*
 Copyright (c) 2024-2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       ContentType.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.06.25, 17:49
 */

package dev.bittim.valolink.content.domain.model.contract.content

import dev.bittim.valolink.R
import dev.bittim.valolink.core.ui.util.UiText

enum class ContentType(
    val displayName: UiText,
    val internalName: String,
) {
    SEASON(UiText.StringResource(R.string.contentType_season), "Season"),
    EVENT(UiText.StringResource(R.string.contentType_event), "Event"),
    AGENT(UiText.StringResource(R.string.contentType_agent), "Agent")
}