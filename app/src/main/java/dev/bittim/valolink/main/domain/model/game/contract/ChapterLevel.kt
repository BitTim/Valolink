package dev.bittim.valolink.main.domain.model.game.contract

data class ChapterLevel(
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val reward: Reward,
)
