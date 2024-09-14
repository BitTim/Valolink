package dev.bittim.valolink.main.domain.model.game.contract.chapter

import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward
import dev.bittim.valolink.main.domain.model.user.UserLevel
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
