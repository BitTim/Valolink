package dev.bittim.valolink.content.domain.model.weapon.stats

data class WeaponDamageRange(
    val rangeStartMeters: Double,
    val rangeEndMeters: Double,
    val headDamage: Double,
    val bodyDamage: Double,
    val legDamage: Double,
)
