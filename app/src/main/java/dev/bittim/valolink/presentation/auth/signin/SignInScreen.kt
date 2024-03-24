package dev.bittim.valolink.presentation.auth.signin

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.R
import dev.bittim.valolink.ui.theme.ValolinkTheme

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign in to Valolink",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.padding(8.dp))

        FilledTonalButton(
            onClick = {},
        ) {
            Icon(
                modifier = Modifier.padding(
                    start = 12.dp,
                    top = 10.dp,
                    end = 10.dp,
                    bottom = 10.dp
                ),
                painter = painterResource(id = R.drawable.ic_google_logo),
                tint = Color.Unspecified,
                contentDescription = "Google logo"
            )

            Text(
                modifier = Modifier.padding(end = 12.dp),
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    ValolinkTheme {
        SignInScreen(
            state = SignInState(),
            onSignInClick = {}
        )
    }
}