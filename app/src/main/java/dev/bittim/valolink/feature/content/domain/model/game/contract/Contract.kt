package dev.bittim.valolink.feature.content.domain.model.game.contract

import java.util.Random

data class Contract(
    val uuid: String,
    val displayName: String,
    val useLevelVPCostOverride: Boolean,
    val levelVPCostOverride: Int, val content: Content
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
}