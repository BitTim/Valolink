/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       ActivityDestination.kt
 * Module:     Valolink.shared.commonMain
 * Author:     Tim Anhalt (BitTim)
 * Modified:   22.06.26, 16:36
 */

package dev.bittim.valolink.feature.activity.nav

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.bittim.valolink.core.nav.AuthenticatedDestination
import dev.bittim.valolink.core.nav.fadeThrough
import dev.bittim.valolink.core.nav.navigateBack
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowScreen
import dev.bittim.valolink.feature.activity.ui.screen.addFlow.ActivityAddFlowViewModel
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListScreen
import dev.bittim.valolink.feature.activity.ui.screen.list.ActivityListViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.koin.compose.viewmodel.koinViewModel

interface ActivityDestination : AuthenticatedDestination

val activitySerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(ActivityListScreenNav::class)
        subclass(ActivityAddFlow::class)
    }
}

@Serializable
data object ActivityListScreenNav : ActivityDestination {
    override val showBottomNav: Boolean get() = true

}

@Serializable
data object ActivityAddFlow : ActivityDestination

fun EntryProviderScope<NavKey>.activityDestination(
    backStack: NavBackStack<NavKey>
) {
    entry<ActivityListScreenNav>(metadata = fadeThrough) {
        val activityListViewModel = koinViewModel<ActivityListViewModel>()
        val activityListState by activityListViewModel.state.collectAsStateWithLifecycle()

        ActivityListScreen(
            state = activityListState
        )
    }

    entry<ActivityAddFlow> {
        val activityAddFlowViewModel = koinViewModel<ActivityAddFlowViewModel>()
        val activityAddFlowState by activityAddFlowViewModel.state.collectAsStateWithLifecycle()

        ActivityAddFlowScreen(
            state = activityAddFlowState,
            onAction = { activityAddFlowViewModel.onAction(it, { backStack.navigateBack() }) }
        )
    }
}