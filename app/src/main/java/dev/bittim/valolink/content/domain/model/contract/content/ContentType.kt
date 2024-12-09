package dev.bittim.valolink.content.domain.model.contract.content

enum class ContentType(
    val displayName: String,
    val internalName: String,
) {
    SEASON("Season", "Season"),
    EVENT("Event", "Event"),
    AGENT("Recruit", "Agent")
}