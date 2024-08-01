package dev.bittim.valolink.main.domain.model.game.contract

import dev.bittim.valolink.main.domain.model.game.contract.content.Content
import dev.bittim.valolink.main.domain.model.user.UserContract
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