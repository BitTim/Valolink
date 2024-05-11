package dev.bittim.valolink.feature.content.domain.model.weapon

import dev.bittim.valolink.feature.content.domain.model.contract.RewardRelation

data class WeaponSkinLevel(
    val uuid: String,
    val displayName: String,
    val levelItem: String?,
    val displayIcon: String,
    val streamedVideo: String?
) {
    fun asRewardRelation(): RewardRelation {
        return RewardRelation(
            uuid = uuid, type = "Weapon Skin", displayName = displayName, displayIcon = displayIcon
        )
    }
}
