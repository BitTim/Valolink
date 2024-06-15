package dev.bittim.valolink.main.domain.model.game.weapon.stats

data class WeaponDamageRange(
    val rangeStartMeters: Double,
    val rangeEndMeters: Double,
    val headDamage: Double,
    val bodyDamage: Double,
    val legDamage: Double,
)
