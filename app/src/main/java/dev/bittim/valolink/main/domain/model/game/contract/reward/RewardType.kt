package dev.bittim.valolink.main.domain.model.game.contract.reward

enum class RewardType(
    val displayName: String,
) {
    BUDDY("Buddy"), CURRENCY("Currency"), PLAYER_CARD("Player Card"), TITLE("Title"), SPRAY("Spray"), WEAPON_SKIN("Weapon Skin")
}