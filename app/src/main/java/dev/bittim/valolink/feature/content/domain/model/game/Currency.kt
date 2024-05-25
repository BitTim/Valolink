package dev.bittim.valolink.feature.content.domain.model.game

import dev.bittim.valolink.feature.content.domain.model.game.contract.RewardRelation

data class Currency(
    val uuid: String,
    val displayName: String,
    val displayNameSingular: String,
    val displayIcon: String,
    val largeIcon: String
) {
    companion object {
        const val DOUGH_UUID = "85ca954a-41f2-ce94-9b45-8ca3dd39a00d"
        const val VP_UUID = "85ad13f7-3d1b-5128-9eb2-7cd8ee0b5741"
        const val RADIANITE_UUID = "e59aa87c-4cbf-517a-5983-6e81511be9b7"
    }



    fun asRewardRelation(amount: Int): RewardRelation {
        val actualAmount = if (uuid == RADIANITE_UUID) amount * 10 else amount
        
        return RewardRelation(
            uuid = uuid,
            type = "Currency",
            displayName = if (actualAmount == 1) displayNameSingular else displayName,
            displayIcon = displayIcon,
            amount = actualAmount
        )
    }
}