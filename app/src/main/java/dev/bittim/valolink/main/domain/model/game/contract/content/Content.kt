package dev.bittim.valolink.main.domain.model.game.contract.content

import dev.bittim.valolink.main.domain.model.game.contract.chapter.Chapter

data class Content(
    val relation: ContentRelation?,
    val premiumVPCost: Int,
    val chapters: List<Chapter>,
)