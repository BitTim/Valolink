package dev.bittim.valolink.main.domain.model.game.contract

data class Content(
    val relation: ContentRelation?,
    val premiumVPCost: Int,
    val chapters: List<Chapter>,
)