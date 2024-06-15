package dev.bittim.valolink.main.domain.model.game.contract.chapter

import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward

data class ChapterLevel(
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val reward: Reward,
)
