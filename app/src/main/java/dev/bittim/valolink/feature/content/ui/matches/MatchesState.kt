package dev.bittim.valolink.feature.content.ui.matches

import dev.bittim.valolink.feature.content.data.remote.game.dto.VersionDto

sealed interface MatchesState {
    data object Fetching : MatchesState
    data object Loading : MatchesState
    data class Content(
        val version: VersionDto?,
        val error: Boolean
    ) : MatchesState
}