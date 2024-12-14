/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RewardType.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.contract.reward

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Palette
import androidx.compose.ui.graphics.vector.ImageVector

enum class RewardType(
    val displayName: String,
    val internalName: String,
    val icon: ImageVector,
) {
    BUDDY("Buddy", "EquippableCharmLevel", Icons.Filled.Loyalty),
    CURRENCY("Currency", "Currency", Icons.Filled.AttachMoney),
    PLAYER_CARD("Player Card", "PlayerCard", Icons.Filled.Image),
    TITLE("Title", "Title", Icons.Filled.MilitaryTech),
    SPRAY("Spray", "Spray", Icons.Filled.FormatPaint),
    WEAPON_SKIN("Weapon Skin", "EquippableSkinLevel", Icons.Filled.Palette),
    AGENT("Agent", "Agent", Icons.Filled.EmojiPeople)
}