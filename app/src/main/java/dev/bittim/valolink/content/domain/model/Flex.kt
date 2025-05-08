/*
 Copyright (c) 2025 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Flex.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   08.05.25, 13:38
 */

package dev.bittim.valolink.content.domain.model

import dev.bittim.valolink.content.domain.model.contract.reward.RewardRelation
import dev.bittim.valolink.content.domain.model.contract.reward.RewardType

data class Flex(
    val uuid: String,
    val displayName: String,
    val displayIcon: String,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = RewardType.FLEX,
            amount = amount,
            displayName = displayName,
            displayIcon = displayIcon,
            previewImages = listOf(displayIcon to null),
            background = displayIcon
        )
    }
}
