/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       Contract.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:48
 */

package dev.bittim.valolink.content.domain.model.contract

import dev.bittim.valolink.content.domain.model.contract.content.Content
import dev.bittim.valolink.user.domain.model.UserContract
import java.util.Random
import java.util.UUID

data class Contract(
    val uuid: String,
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

    fun toUserObj(uid: String): UserContract {
        return UserContract(
            UUID.randomUUID().toString(),
            uid,
            uuid,
            emptyList()
        )
    }
}