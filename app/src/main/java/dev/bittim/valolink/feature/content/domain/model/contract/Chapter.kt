package dev.bittim.valolink.feature.content.domain.model.contract

data class Chapter(
    val levels: List<ChapterLevel>,
    val freeRewards: List<Reward>?,
    val isEpilogue: Boolean,
)
