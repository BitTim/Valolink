package dev.bittim.valolink.main.domain.model.game.contract.content

enum class ContentType(
    val displayName: String,
    val internalName: String,
) {
    SEASON("Season", "Season"),
    EVENT("Event", "Event"),
    AGENT("Recruit", "Agent")
}