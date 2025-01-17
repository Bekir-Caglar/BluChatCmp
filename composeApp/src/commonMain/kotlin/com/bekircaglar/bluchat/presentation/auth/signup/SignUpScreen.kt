package com.bekircaglar.bluchat.presentation.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.navigation.Screens
import com.bekircaglar.bluchat.presentation.auth.component.AuthButton
import com.bekircaglar.bluchat.presentation.auth.component.AuthTextField
import com.bekircaglar.bluchat.presentation.auth.component.PhoneVisualTransformation
import com.bekircaglar.bluchat.presentation.component.ChatAppTopBar
import com.bekircaglar.bluchat.presentation.component.ToastNotificationDialog
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import com.bekircaglar.bluchat.utils.UiState
import com.bekircaglar.bluchat.utils.error
import com.bekircaglar.bluchat.utils.isError
import com.bekircaglar.bluchat.utils.isLoading
import com.bekircaglar.bluchat.utils.isSuccess
import com.bekircaglar.bluchat.utils.passwordBorder
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(navController: NavController) {

    val viewModel: SignUpViewModel = koinViewModel()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var emailRegister by remember { mutableStateOf("") }
    var passwordRegister by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val toastNotificationManager = remember { ToastNotificationManager() }
    val passwordError by remember { mutableStateOf<String?>(null) }


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val signUpState by viewModel.signUpState.collectAsStateWithLifecycle()
    val notification by toastNotificationManager.notificationFlow.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = signUpState) {
        if (signUpState.isSuccess) {
            navController.navigate(Screens.HomeNav.route) {
                popUpTo(Screens.AuthNav.route) {
                    inclusive = true
                }
            }
        }
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
                            text = "Sign Up",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(end = 30.dp),
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                navigationIcon = Icons.Default.KeyboardArrowLeft,
                onNavigateIconClicked = {
                    navController.navigate(Screens.SingInScreen.route)
                },
                searchIcon = false
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.weight(1f)
                ) {
                    AuthTextField(
                        hint = "Name",
                        value = name,
                        onValueChange = { name = it.replaceFirstChar { char -> char.uppercase() } },
                        leadingIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        title = "Name"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier.weight(1f)
                ) {
                    AuthTextField(
                        hint = "Surname",
                        value = surname,
                        onValueChange = {
                            surname = it.replaceFirstChar { char -> char.uppercase() }
                        },
                        leadingIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        title = "Surname"
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                AuthTextField(
                    hint = "Enter your phone number",
                    value = phoneNumber,
                    onValueChange = { if (it.length <= 10) phoneNumber = it },
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    title = "Phone Number",
                    visualTransformation = PhoneVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                AuthTextField(
                    hint = "Enter your email",
                    value = emailRegister,
                    onValueChange = { emailRegister = it },
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    title = "Email"
                )
            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AuthTextField(
                        hint = "Enter your password",
                        value = passwordRegister,
                        onValueChange = { passwordRegister = it },
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        title = "Password",
                        supportedTextList = listOf(
                            "8 characters" to (passwordRegister.length >= 8),
                            "Minimum one number" to passwordRegister.any { it.isDigit() },
                            "Minimum one uppercase letter" to passwordRegister.any { it.isUpperCase() },
                            "Minimum one lowercase letter" to passwordRegister.any { it.isLowerCase() }
                        )
                    )
                    Spacer(modifier = Modifier.padding(top = 8.dp))

                    AuthTextField(
                        hint = "Confirm password",
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        title = "Confirm password",
                        modifier = Modifier.passwordBorder(passwordRegister == confirmPassword),
                    )

                    Spacer(modifier = Modifier.padding(top = 32.dp))

                    AuthButton(
                        onClick = {
                            if (passwordRegister != confirmPassword) {
                                toastNotificationManager.showNotification(
                                    notificationData = NotificationData(
                                        message = "Passwords do not match",
                                        type = NotificationType.Error
                                    )
                                )
                            } else {
                                if (emailRegister.isNotEmpty() && passwordRegister.isNotEmpty() && phoneNumber.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()) {
                                    if (viewModel.isPasswordValid(passwordRegister)) {

                                        viewModel.updateEmail(emailRegister)
                                        viewModel.updatePassword(passwordRegister)
                                        viewModel.updatePhoneNumber(phoneNumber)
                                        viewModel.updateName(name)
                                        viewModel.updateSurname(surname)
                                        viewModel.signUp()
                                    } else {
                                        toastNotificationManager.showNotification(
                                            notificationData = NotificationData(
                                                message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter and one number",
                                                type = NotificationType.Warning
                                            )
                                        )
                                    }

                                } else {
                                    toastNotificationManager.showNotification(
                                        notificationData = NotificationData(
                                            message = "Please fill all fields",
                                            type = NotificationType.Warning
                                        )
                                    )
                                }
                            }

                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.background,
                        buttonText = "Sign Up",
                    )

                }

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


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        var isVisible by remember { mutableStateOf(notification != null) }

        LaunchedEffect(notification) {
            if (notification != null) {
                isVisible = true
                delay(3000)
                isVisible = false
            }
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        ) {
            notification?.let { notificationData ->
                ToastNotificationDialog(
                    notificationData = notificationData,
                    onDismiss = {
                        isVisible = false
                    }
                )
            }
            if (!isVisible){
                toastNotificationManager.dismissNotification()
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}