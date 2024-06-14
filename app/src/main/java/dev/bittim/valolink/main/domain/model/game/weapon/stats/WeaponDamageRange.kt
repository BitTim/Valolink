package dev.bittim.valolink.main.domain.model.game.weapon.stats

data class WeaponDamageRange(
    val rangeStartMeters: Float,
    val rangeEndMeters: Float,
    val headDamage: Float,
    val bodyDamage: Float,
    val legDamage: Float,
)
