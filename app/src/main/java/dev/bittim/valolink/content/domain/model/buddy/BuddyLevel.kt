package dev.bittim.valolink.content.domain.model.buddy

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
