package dev.bittim.valolink.content.domain.model.weapon.shopData

data class WeaponShopData(
    val cost: Int,
    val category: String,
    val shopOrderPriority: Int,
    val categoryText: String,
    val gridPosition: WeaponGridPosition?,
    val canBeTrashed: Boolean,
    val image: String?,
    val newImage: String,
    val newImage2: String?,
)
