package dev.bittim.valolink.core.ui.nav.actions

import android.os.Parcelable
import androidx.navigation.NavOptions

interface NavAction {
    val route: String
    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}