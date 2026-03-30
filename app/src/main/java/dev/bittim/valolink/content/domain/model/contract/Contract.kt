/*
 Copyright (c) 2024-2026 Tim Anhalt (BitTim)

 Project:    Valolink
 License:    GPLv3

 File:       Contract.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   30.03.26, 03:33
 */

package dev.bittim.valolink.content.domain.model.contract

import dev.bittim.valolink.content.domain.model.contract.content.Content
import dev.bittim.valolink.user.domain.model.UserContract
import dev.bittim.valolink.user.domain.model.UserLevel
import java.util.Random
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Contract @OptIn(ExperimentalUuidApi::class) constructor(
    val uuid: Uuid,
    val displayName: String,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int,
    val content: Content,
) {
    fun calcTotalXp(): Int {
        return content.chapters.map { chapter ->
            chapter.levels.map { level ->
                level.xp
            }
        }.flatten().sum()
    }

    fun calcLevelCount(): Int {
        return content.chapters.sumOf { chapter ->
            chapter.levels.count()
        }
    }

    // TODO: Placeholder function to generate dummy data for UI
    fun getRandomCollectedXP(): Int {
        val random = Random()
        return random.nextInt(calcTotalXp())
    }

    fun toUserObj(
        uid: String,
        freeOnly: Boolean,
        levels: List<UserLevel> = emptyList()
    ): UserContract {
        return UserContract(
            UUID.randomUUID().toString(),
            uid,
            uuid,
            levels,
            freeOnly
        )
    }
}
