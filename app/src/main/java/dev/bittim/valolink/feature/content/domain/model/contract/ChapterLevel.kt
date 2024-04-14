package dev.bittim.valolink.feature.content.domain.model.contract

data class ChapterLevel(
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val reward: Reward
)
