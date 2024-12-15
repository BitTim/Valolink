/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       RewardRelation.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.contract.reward

data class RewardRelation(
    val uuid: String,
    val type: RewardType,
    val amount: Int,
    val displayName: String,
    val displayIcon: String,
    val previewImages: List<Pair<String?, Any?>>,
    val background: String? = null,
)