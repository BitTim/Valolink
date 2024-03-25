package dev.bittim.valolink.core.ui.nav.navigator

import dev.bittim.valolink.core.ui.nav.actions.NavAction
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val navActions: StateFlow<NavAction?>
    fun navigate(navAction: NavAction?)
}