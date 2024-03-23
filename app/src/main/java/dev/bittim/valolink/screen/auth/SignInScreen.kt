package dev.bittim.valolink.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun SignInScreen(
    onSignInClick:() -> Unit,
    onSignUpClick:() -> Unit,
    onForgotPasswordClick:() -> Unit
) {
     Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // This should not matter
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
     ) {
        Button(onClick = { onSignInClick.invoke() }) {
            Text(text = "Sign In")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    ValolinkTheme {
        SignInScreen(
            onSignInClick = {},
            onSignUpClick = {},
            onForgotPasswordClick = {}
        )
    }
}