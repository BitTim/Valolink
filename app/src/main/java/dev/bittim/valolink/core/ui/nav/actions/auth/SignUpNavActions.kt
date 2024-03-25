package dev.bittim.valolink.core.ui.nav.actions.auth

import androidx.navigation.NavOptions
import dev.bittim.valolink.core.ui.nav.Routes
import dev.bittim.valolink.core.ui.nav.actions.NavAction

object SignUpNavActions {
    fun toSignIn() = object : NavAction {
        override val route = Routes.Auth.SignIn.route
        override val navOptions = NavOptions.Builder()
            .setPopUpTo(Routes.Auth.SignUp.route, true)
            .build()
    }
}