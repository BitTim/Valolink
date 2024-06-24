package dev.bittim.valolink.main.domain.model.game

data class Version(
    val manifestId: String,
    val branch: String,
    val version: String,
    val buildVersion: String,
    val engineVersion: String,
    val riotClientVersion: String,
    val riotClientBuild: String,
    val buildDate: String,
) {
    companion object {
        val EMPTY = Version(
            manifestId = "",
            branch = "",
            version = "",
            buildVersion = "",
            engineVersion = "",
            riotClientVersion = "",
            riotClientBuild = "",
            buildDate = ""
        )
    }
}
