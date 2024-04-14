package dev.bittim.valolink.feature.content.domain.model.contract

data class Chapter(
    val levels: List<Level>,
    val freeRewards: List<Reward>?,
    val isEpilogue: Boolean,
)
