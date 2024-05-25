package dev.bittim.valolink.feature.main.domain.model.game.contract

data class Chapter(
    val levels: List<ChapterLevel>,
    val freeRewards: List<Reward>?,
    val isEpilogue: Boolean,
)
