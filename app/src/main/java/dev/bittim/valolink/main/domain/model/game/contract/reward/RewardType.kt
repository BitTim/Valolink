package dev.bittim.valolink.main.domain.model.game.contract.reward

enum class RewardType(
    val displayName: String,
    val internalName: String,
) {
    BUDDY("Buddy", "EquippableCharmLevel"),
    CURRENCY("Currency", "Currency"),
    PLAYER_CARD("Player Card", "PlayerCard"),
    TITLE("Title", "Title"),
    SPRAY("Spray", "Spray"),
    WEAPON_SKIN("Weapon Skin", "EquippableSkinLevel")
}