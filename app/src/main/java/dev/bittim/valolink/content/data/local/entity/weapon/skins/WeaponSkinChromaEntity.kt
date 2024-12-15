/*
 Copyright (c) 2024 Tim Anhalt (BitTim)
 
 Project:    Valolink
 License:    GPLv3
 
 File:       WeaponSkinChromaEntity.kt
 Module:     Valolink.app.main
 Author:     Tim Anhalt (BitTim)
 Modified:   14.12.24, 14:47
 */

package dev.bittim.valolink.content.data.local.entity.weapon.skins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.bittim.valolink.content.data.local.entity.VersionedEntity
import dev.bittim.valolink.content.domain.model.weapon.skins.WeaponSkinChroma

@Entity(
    tableName = "WeaponSkinChromas", foreignKeys = [ForeignKey(
        entity = WeaponSkinEntity::class,
        parentColumns = ["uuid"],
        childColumns = ["weaponSkin"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(
        value = ["uuid"], unique = true
    ), Index(
        value = ["weaponSkin"], unique = false
    )]
)
data class WeaponSkinChromaEntity(
    @PrimaryKey val uuid: String,
    override val version: String,
    val weaponSkin: String,
    val chromaIndex: Int,
    val displayName: String,
    val displayIcon: String?,
    val fullRender: String,
    val swatch: String?,
    val streamedVideo: String?,
) : VersionedEntity {
    fun toType(): WeaponSkinChroma {
        return WeaponSkinChroma(
            uuid = uuid,
            chromaIndex = chromaIndex,
            displayName = displayName,
            displayIcon = displayIcon,
            fullRender = fullRender,
            swatch = swatch,
            streamedVideo = streamedVideo,
        )
    }
}
