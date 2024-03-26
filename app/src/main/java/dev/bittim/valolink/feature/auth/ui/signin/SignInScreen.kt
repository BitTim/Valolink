package dev.bittim.valolink.feature.auth.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.feature.auth.ui.components.OutlinedTextFieldWithError

@Composable
fun SignInScreen(
    state: SignInState,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onSignInClicked: () -> Unit,
    onNavContent: () -> Unit,
    onNavSignUp: () -> Unit,
    onNavForgot: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is SignInState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Signing in",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.padding(16.dp))
                    CircularProgressIndicator()
                }
            }
            is SignInState.Success -> {
                LaunchedEffect(key1 = Unit) {
                    onNavContent()
                }
            }

            is SignInState.Input -> {
                Text(
                    text = "Sign in to Valolink",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.padding(16.dp))

                OutlinedTextFieldWithError(
                    label = "Email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = "Email"
                        )
                    },
                    value = state.email,
                    error = state.emailError,
                    enableVisibilityToggle = false,
                    onValueChange = onEmailValueChange
                )

                Spacer(modifier = Modifier.padding(8.dp))

                OutlinedTextFieldWithError(
                    label = "Password",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Password,
                            contentDescription = "Password"
                        )
                    },
                    value = state.password,
                    error = state.passwordError,
                    enableVisibilityToggle = true,
                    onValueChange = onPasswordValueChange
                )

                Spacer(modifier = Modifier.padding(8.dp))

                if (state.authError != null) {
                    Text(
                        text = state.authError,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignInClicked
                ) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onNavForgot
                ) {
                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(16.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    FilledTonalButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        onClick = onNavSignUp
                    ) {
                        Text(
                            text = "Sign up",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingSignInScreenPreview() {
    SignInScreen(
        state = SignInState.Loading,
        {}, {}, {}, {}, {}, {}
    )
}

@Preview(showSystemUi = true)
@Composable
fun InputSignInScreenPreview() {
    SignInScreen(
        state = SignInState.Input(
            email = "",
            password = "",
            emailError = null,
            passwordError = null,
            authError = null
        ),
        {}, {}, {}, {}, {}, {}
    )
}

@Preview(showSystemUi = true)
@Composable
fun ErrorInputSignInScreenPreview() {
    SignInScreen(
        state = SignInState.Input(
            email = "test@mailcom",
            password = "Password",
            emailError = "",
            passwordError = "",
            authError = "Something went wrong"
        ),
        {}, {}, {}, {}, {}, {}
    )
}