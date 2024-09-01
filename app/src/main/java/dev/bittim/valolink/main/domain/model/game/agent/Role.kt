package dev.bittim.valolink.main.domain.model.game.agent

data class Role(
    val uuid: String,
    val displayName: String,
    val description: String,
    val displayIcon: String,
) {
    companion object {
        val EMPTY = Role(
            uuid = "",
            displayName = "",
            description = "",
            displayIcon = ""
        )
    }
}
