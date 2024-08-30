package dev.bittim.valolink.main.domain.model.game.buddy

data class BuddyLevel(
    val uuid: String,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
) {
    companion object {
        val EMPTY = BuddyLevel(
            uuid = "",
            hideIfNotOwned = false,
            displayName = "",
            displayIcon = ""
        )
    }
}
