package dev.bittim.valolink.core.ui.nav.actions.auth

import androidx.navigation.NavOptions
import dev.bittim.valolink.core.ui.nav.Routes
import dev.bittim.valolink.core.ui.nav.actions.NavAction

object SignInNavActions {
    fun toContent() = object : NavAction {
        override val route = Routes.Content.route
        override val navOptions = NavOptions.Builder()
            .setPopUpTo(Routes.Auth.route, true)
            .build()
    }

    fun toSignUp() = object : NavAction {
        override val route = Routes.Auth.SignUp.route
    }

    fun toForgot() = object : NavAction {
        override val route = Routes.Auth.Forgot.route
    }
}