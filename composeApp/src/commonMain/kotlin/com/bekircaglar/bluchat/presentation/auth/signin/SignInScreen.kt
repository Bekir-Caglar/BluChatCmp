package com.bekircaglar.bluchat.presentation.auth.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekircaglar.bluchat.AppContext
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.navigation.Screens
import com.bekircaglar.bluchat.presentation.auth.component.AuthButton
import com.bekircaglar.bluchat.presentation.auth.component.AuthTextField
import com.bekircaglar.bluchat.presentation.auth.component.EnterPhoneNumberDialog
import com.bekircaglar.bluchat.presentation.auth.component.LoginFacebookButton
import com.bekircaglar.bluchat.presentation.component.ChatAppTopBar
import com.bekircaglar.bluchat.presentation.component.ToastNotificationDialog
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.data
import androidx.compose.runtime.*
import com.bekircaglar.bluchat.presentation.component.ToastNotificationComponent
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import com.bekircaglar.bluchat.utils.error
import com.bekircaglar.bluchat.utils.isError
import com.bekircaglar.bluchat.utils.isLoading
import com.bekircaglar.bluchat.utils.isSuccess
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.google.GoogleUser
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = koinViewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = AppContext
    val toastNotificationManager = remember { ToastNotificationManager() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()
    val signInUIState by viewModel.signInUIState.collectAsStateWithLifecycle()
    val authResultState by viewModel.authResultState.collectAsStateWithLifecycle()
    val showPhoneNumberDialog by viewModel.showPhoneNumberDialog.collectAsStateWithLifecycle()
    val notification by toastNotificationManager.notificationFlow.collectAsStateWithLifecycle()

    val clearUiState = viewModel::clearState

    LaunchedEffect(signInState) {
        if (signInState.isSuccess) {
            navController.navigate(Screens.HomeNav.route) {
                popUpTo(Screens.AuthNav.route) {
                    inclusive = true
                }
            }
        }
    }

    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = "639006118135-akudk82fulrvbl47um2dsoi815corrsu.apps.googleusercontent.com"))

    if (showPhoneNumberDialog) {
        EnterPhoneNumberDialog(
            onConfirm = { phoneNumber ->
                viewModel.updatePhoneNumber(phoneNumber)
                viewModel.dismissPhoneNumberDialog()
                viewModel.createGoogleUser()
            },
            onDismiss = {
                viewModel.dismissPhoneNumberDialog()
                viewModel.deleteCurrentUser()
                viewModel.signOut()
            }
        )
    }


    Scaffold(
        topBar = {
            ChatAppTopBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Login",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(end = 30.dp)
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                searchIcon = false
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = contentPadding)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                AuthTextField(
                    hint = "Enter your email",
                    value = email,
                    onValueChange = { email = it },
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    title = "Email"
                )
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AuthTextField(
                        hint = "Enter your password",
                        value = password,
                        onValueChange = { password = it },
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        title = "Password"
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {

                        }) {
                            Text(
                                text = "Forgot password",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    AuthButton(
                        onClick = {
                            if (email.isEmpty() || password.isEmpty()) {
                                toastNotificationManager.showNotification(
                                    notificationData = NotificationData(
                                        message = "Please fill all fields",
                                        type = NotificationType.Warning
                                    )
                                )
                                return@AuthButton
                            }
                            viewModel.updateSignInEmail(email)
                            viewModel.updateSignInPassword(password)
                            viewModel.signIn()
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background,
                        buttonText = "Login",
                    )

                    Spacer(modifier = Modifier.padding(top = 8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = {
                                navController.navigate(Screens.SingUpScreen.route)
                            }
                        ) {
                            Text(
                                text = "Don't have an account?",
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                        }
                    }

                    Text(
                        text = "By continuing you agree to our Terms of Service and Privacy Policy",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 24.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    HorizontalDivider(modifier = Modifier)
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))

                    GoogleButtonUiContainer(
                        onGoogleSignInResult = { googleUser ->
                            googleUser?.let { user ->
                                viewModel.setGoogleUser(googleUser = user)
                                viewModel.signInWithGoogle()
                            }
                        }
                    ) {
                        GoogleSignInButton(
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .width(350.dp)
                                .height(50.dp),
                            onClick = { this.onClick() }
                        )
                    }

                    LoginFacebookButton(
                        onAuthError = {},
                        buttonText = "Login with Facebook",
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }
        }
        LaunchedEffect(uiState) {
            if (uiState.isError) {
                toastNotificationManager.showNotification(
                    notificationData = NotificationData(
                        message = uiState.error.toString(),
                        type = NotificationType.Error
                    )
                )
            }
        }
        LaunchedEffect(signInState) {
            if (signInState.isError) {
                toastNotificationManager.showNotification(
                    notificationData = NotificationData(
                        message = signInState.error.toString(),
                        type = NotificationType.Error
                    )
                )
            }
        }

        ToastNotificationComponent(
            notification = notification,
            toastNotificationManager = toastNotificationManager,
            topPadding = contentPadding.calculateTopPadding() - 10.dp
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
