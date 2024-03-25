package dev.bittim.valolink.core.ui.nav

sealed class Routes(val route: String) {
    data object Root : Routes("root")

    data object Auth : Routes("auth") {
        data object SignIn : Routes("signin")
        data object SignUp : Routes("signup")
        data object Forgot : Routes("forgot")
    }

    data object Content : Routes("content") {
        data object Home : Routes("home")
        data object Contracts : Routes("contracts")
        data object Matches : Routes("matches")
        data object Friends : Routes("friends")
    }
}