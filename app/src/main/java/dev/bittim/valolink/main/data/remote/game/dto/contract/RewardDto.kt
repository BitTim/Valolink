package dev.bittim.valolink.main.data.remote.game.dto.contract

import dev.bittim.valolink.main.data.local.game.entity.contract.RewardEntity
import java.util.UUID

data class RewardDto(
    val type: String,
    val uuid: String,
    val amount: Int,
    val isHighlighted: Boolean,
) {
    fun toEntity(
        version: String,
        chapterUuid: String? = null,
        levelUuid: String? = null,
    ): RewardEntity {
        return RewardEntity(
            chapterUuid ?: levelUuid ?: UUID.randomUUID().toString(),
            levelUuid,
            chapterUuid,
            version,
            type,
            uuid,
            amount,
            isHighlighted
        )
    }
}