package dev.bittim.valolink.main.domain.model.game.contract.chapter

import dev.bittim.valolink.main.domain.model.game.contract.reward.Reward

data class Level(
    val uuid: String,
    val name: String,
    val contractName: String,
    val xp: Int,
    val vpCost: Int,
    val isPurchasableWithVP: Boolean,
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val reward: Reward,
)
