package dev.bittim.valolink.content.data.local.entity

abstract class GameEntity : VersionedEntity {
    abstract val version: String

    override fun getApiVersion(): String {
        return version
    }
}