package dev.bittim.valolink.feature.content.data.local.game.entity

abstract class GameEntity<T> {
    abstract val version: String
    abstract fun toType(): T
}