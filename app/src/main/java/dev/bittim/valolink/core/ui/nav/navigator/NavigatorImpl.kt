package dev.bittim.valolink.core.ui.nav.navigator

import dev.bittim.valolink.core.ui.nav.actions.NavAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigatorImpl : Navigator {
    private val _navActions: MutableStateFlow<NavAction?> by lazy {
        MutableStateFlow(null)
    }

    override val navActions = _navActions.asStateFlow()

    override fun navigate(navAction: NavAction?) {
        _navActions.update { navAction }
    }
}