package dev.bittim.valolink.content.domain.model.contract.content

import dev.bittim.valolink.content.domain.model.contract.chapter.Chapter

data class Content(
    val relation: ContentRelation?,
    val premiumVPCost: Int,
    val chapters: List<Chapter>,
)