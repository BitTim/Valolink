package dev.bittim.valolink.main.domain.model.game.contract.chapter

import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward

data class Chapter(
    val levels: List<Level>,
    val freeRewards: List<Reward>?,
    val isEpilogue: Boolean,
)
