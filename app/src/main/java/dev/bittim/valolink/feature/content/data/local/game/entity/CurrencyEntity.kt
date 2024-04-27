package dev.bittim.valolink.feature.content.data.local.game.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.domain.model.Currency

@Entity(tableName = "Currencies")
data class CurrencyEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val displayNameSingular: String,
    val displayIcon: String,
    val largeIcon: String
) : GameEntity() {
    fun toType(): Currency {
        return Currency(
            uuid, displayName, displayNameSingular, displayIcon, largeIcon
        )
    }
}