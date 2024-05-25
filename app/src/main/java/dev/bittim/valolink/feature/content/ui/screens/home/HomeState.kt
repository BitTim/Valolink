package dev.bittim.valolink.feature.content.ui.screens.home

import dev.bittim.valolink.feature.content.domain.model.UserData
import io.appwrite.models.Preferences

data class HomeState(
    val isLoading: Boolean = false,
    val username: String? = null,
    val userPrefs: Preferences<Map<String, Any>>? = null,
    val userData: UserData? = null,
)