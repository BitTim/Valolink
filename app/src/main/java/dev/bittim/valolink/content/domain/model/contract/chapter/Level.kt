/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Level.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:29
 */

package dev.bittim.valolink.content.domain.model.contract.chapter

import dev.bittim.valolink.content.domain.model.contract.reward.Reward
import dev.bittim.valolink.user.domain.model.UserLevel
import java.util.UUID

data class Level(
    val uuid: String,
    val dependency: String?,
    val name: String,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val rewards: List<Reward>,
) {
    fun toUserObj(userContract: String, isPurchased: Boolean): UserLevel {
        return UserLevel(
            UUID.randomUUID().toString(),
            userContract,
            uuid,
            isPurchased
        )
    }

    companion object {
        fun reverseTraverse(
            levels: List<Level>,
            start: String?,
            end: String?,
            includeEnd: Boolean,
            isFirstCall: Boolean = true,
        ): List<Level> {
            val startLevel = levels.firstOrNull { it.uuid == start }
            if (startLevel == null) return emptyList()

            val startList = listOf(startLevel)

            if (start == end) return if (includeEnd) startList else emptyList()
            val result = startList.plus(
                reverseTraverse(
                    levels,
                    startLevel.dependency,
                    end,
                    includeEnd,
                    false
                )
            )

            if (isFirstCall) return result.reversed()
            return result
        }
    }
}
