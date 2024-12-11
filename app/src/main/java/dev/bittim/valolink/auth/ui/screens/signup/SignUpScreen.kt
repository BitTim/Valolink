package dev.bittim.valolink.auth.ui.screens.signup

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bittim.valolink.core.ui.components.OutlinedTextFieldWithError
import dev.bittim.valolink.core.ui.theme.ValolinkTheme

@Composable
fun SignUpScreen(
    state: SignUpState,
    onEmailValueChange: (String) -> Unit,
    onUsernameValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    onSignUpClicked: () -> Unit,
    onNavToContent: () -> Unit,
    onNavToSignIn: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isSuccess) {
            LaunchedEffect(key1 = Unit) {
                onNavToContent()
            }
        }

        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Creating account",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.padding(16.dp))
                CircularProgressIndicator()
            }
        } else {
            Text(
                text = "Create a new Valolink account",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "After creating an account you will need to connect your Riot Games account in order to use the app.",
                style = MaterialTheme.typography.bodyMedium
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
                label = "Username",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Username"
                    )
                },
                value = state.username,
                error = state.usernameError,
                enableVisibilityToggle = false,
                onValueChange = onUsernameValueChange
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

            OutlinedTextFieldWithError(
                label = "Confirm Password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Password,
                        contentDescription = "Password"
                    )
                },
                value = state.confirmPassword,
                error = state.confirmPasswordError,
                enableVisibilityToggle = true,
                onValueChange = onConfirmPasswordValueChange
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
                onClick = onSignUpClicked
            ) {
                Text(
                    text = "Create account",
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
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium
                )

                FilledTonalButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    onClick = onNavToSignIn
                ) {
                    Text(
                        text = "Sign in",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingSignUpScreenPreview() {
    ValolinkTheme {
        SignUpScreen(state = SignUpState(isLoading = true),
                     {},
                     {},
                     {},
                     {},
                     {},
                     {},
                     {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun InputSignUpScreenPreview() {
    ValolinkTheme {
        SignUpScreen(state = SignUpState(
            email = "",
            username = "",
            password = "",
            confirmPassword = "",
            emailError = null,
            usernameError = null,
            passwordError = null,
            confirmPasswordError = null,
            authError = null
        ),
                     {},
                     {},
                     {},
                     {},
                     {},
                     {},
                     {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErrorInputSignUpScreenPreview() {
    ValolinkTheme {
        SignUpScreen(state = SignUpState(
            email = "",
            username = "",
            password = "",
            confirmPassword = "",
            emailError = "",
            usernameError = "",
            passwordError = "",
            confirmPasswordError = "",
            authError = "Something went wrong"
        ),
                     {},
                     {},
                     {},
                     {},
                     {},
                     {},
                     {})
    }
}