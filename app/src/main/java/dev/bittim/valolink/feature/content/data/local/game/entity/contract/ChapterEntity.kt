package dev.bittim.valolink.feature.content.data.local.game.entity.contract

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.bittim.valolink.feature.content.data.local.game.entity.GameEntity

@Entity
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isEpilogue: Boolean
) : GameEntity {

}