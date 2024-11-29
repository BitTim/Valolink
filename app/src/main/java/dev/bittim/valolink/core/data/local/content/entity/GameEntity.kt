package dev.bittim.valolink.core.data.local.content.entity

abstract class GameEntity : VersionedEntity {
    abstract val version: String

    override fun getApiVersion(): String {
        return version
    }
}