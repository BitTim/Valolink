package dev.bittim.valolink.main.domain.model.game.weapon.skins

import dev.bittim.valolink.main.domain.model.game.contract.RewardRelation

data class WeaponSkinLevel(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String?,
    val streamedVideo: String?,
) {
    fun asRewardRelation(amount: Int): RewardRelation {
        return RewardRelation(
            uuid = uuid,
            type = "Weapon Skin",
            displayName = displayName,
            displayIcon = displayIcon ?: "",
            amount = amount
        )
    }
}
