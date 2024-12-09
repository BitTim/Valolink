package dev.bittim.valolink.content.domain.model.contract.reward

data class RewardRelation(
    val uuid: String,
    val type: RewardType,
    val amount: Int,
    val displayName: String,
    val displayIcon: String,
    val previewImages: List<Pair<String?, Any?>>,
    val background: String? = null,
)