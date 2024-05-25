package dev.bittim.valolink.feature.main.data.local.game.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.main.domain.model.game.Currency

@Entity(
    tableName = "Currencies", indices = [Index(
        value = ["uuid"], unique = true
    )]
)
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