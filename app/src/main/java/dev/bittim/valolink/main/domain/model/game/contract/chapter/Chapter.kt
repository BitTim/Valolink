package dev.bittim.valolink.main.domain.model.game.contract.chapter

data class Chapter(
    val levels: List<Level>,
    val isEpilogue: Boolean,
)
