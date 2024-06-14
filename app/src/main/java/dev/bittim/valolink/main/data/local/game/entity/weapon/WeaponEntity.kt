package dev.bittim.valolink.main.data.local.game.entity.weapon

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.main.data.local.game.entity.GameEntity
import dev.bittim.valolink.main.domain.model.game.weapon.Weapon
import dev.bittim.valolink.main.domain.model.game.weapon.shopData.WeaponShopData
import dev.bittim.valolink.main.domain.model.game.weapon.skins.WeaponSkin
import dev.bittim.valolink.main.domain.model.game.weapon.stats.WeaponStats

@Entity(
    tableName = "Weapons",
    indices = [
        Index(
            value = ["uuid"],
            unique = true
        )
    ]
)
data class WeaponEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val displayName: String,
    val category: String,
    val defaultSkinUuid: String,
    val displayIcon: String,
    val killStreamIcon: String,
    val weaponStats: WeaponStats,
    val shopData: WeaponShopData
) : GameEntity() {
    fun toType(skins: List<WeaponSkin>): Weapon {
        return Weapon(
            uuid = uuid,
            displayName = displayName,
            category = category,
            defaultSkinUuid = defaultSkinUuid,
            displayIcon = displayIcon,
            killStreamIcon = killStreamIcon,
            weaponStats = weaponStats,
            shopData = shopData,
            skins = skins
        )
    }
}
