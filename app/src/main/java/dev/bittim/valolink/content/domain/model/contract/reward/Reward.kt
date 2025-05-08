/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Reward.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.contract.reward

data class Reward(
    val rewardType: String,
    val rewardUuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
    val isFreeReward: Boolean,
    val relation: RewardRelation?,
)
