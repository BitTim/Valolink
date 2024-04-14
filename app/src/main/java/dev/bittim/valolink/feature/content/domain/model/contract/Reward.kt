package dev.bittim.valolink.feature.content.domain.model.contract

data class Reward(
    val type: String,
    val uuid: String,
    val amount: Int,
    val isHighlighted: Boolean
)
