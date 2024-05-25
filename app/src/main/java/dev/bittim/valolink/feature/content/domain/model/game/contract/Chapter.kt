package dev.bittim.valolink.feature.content.domain.model.game.contract

data class Chapter(
    val levels: List<ChapterLevel>,
    val freeRewards: List<Reward>?,
    val isEpilogue: Boolean,
)
