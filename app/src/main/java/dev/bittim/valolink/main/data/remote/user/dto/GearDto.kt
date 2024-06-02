package dev.bittim.valolink.main.data.remote.user.dto

data class GearDto(
    val uuid: String,
    override val updatedAt: String,
    val userUuid: String,
    val contractUuid: String,
    val progress: Int,
) : SyncedDto()
