package com.bekircaglar.bluchat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_account
import bluchatkmp.composeapp.generated.resources.ic_help
import bluchatkmp.composeapp.generated.resources.ic_logout
import bluchatkmp.composeapp.generated.resources.ic_privacy
import bluchatkmp.composeapp.generated.resources.ic_sun
import coil3.compose.AsyncImage
import com.bekircaglar.bluchat.AppContext
import com.bekircaglar.bluchat.LocalTheme
import com.bekircaglar.bluchat.domain.model.NotificationData
import com.bekircaglar.bluchat.domain.model.ProfileTabItem
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.navigation.Screens
import com.bekircaglar.bluchat.presentation.component.ChatAppBottomAppBar
import com.bekircaglar.bluchat.presentation.component.ChatAppTopBar
import com.bekircaglar.bluchat.presentation.component.ToastNotificationComponent
import com.bekircaglar.bluchat.presentation.profile.component.AccountDialog
import com.bekircaglar.bluchat.presentation.profile.component.AppearanceDialog
import com.bekircaglar.bluchat.presentation.profile.component.ProfileMenu
import com.bekircaglar.bluchat.utils.NotificationType
import com.bekircaglar.bluchat.utils.ToastNotificationManager
import com.bekircaglar.bluchat.utils.error
import com.bekircaglar.bluchat.utils.isError
import com.bekircaglar.bluchat.utils.isLoading
import com.bekircaglar.bluchat.utils.isSuccess
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(navController: NavController,) {

    val context = AppContext
    val viewModel: ProfileViewModel = koinViewModel()

    val toastNotificationManager = remember { ToastNotificationManager() }


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profileUserUiState by viewModel.profileUserUiState.collectAsStateWithLifecycle()
    val updateUserState by viewModel.updateUserState.collectAsStateWithLifecycle()
    val logOutState by viewModel.logOutState.collectAsStateWithLifecycle()
    val accountDialogState by viewModel.accountDialogState.collectAsStateWithLifecycle()
    val showAppearanceDialog by viewModel.appearanceDialogState.collectAsStateWithLifecycle()
    val notification by toastNotificationManager.notificationFlow.collectAsStateWithLifecycle()
    val selectedImageUri by viewModel.selectedImageUri.collectAsStateWithLifecycle()
    val uploadedImageUri by viewModel.uploadedImageUri.collectAsStateWithLifecycle()
    val isImageLoading by viewModel.isImageLoading.collectAsStateWithLifecycle()

    val localTheme = LocalTheme.current

    val menuItemList = listOf(
        ProfileTabItem(
            title = "Account",
            icon = Res.drawable.ic_account,
            onClick = {
                viewModel.onAccountDialogShow()
            }
        ),
        ProfileTabItem(
            title = "Appearance",
            icon = Res.drawable.ic_sun,
            onClick = {
                viewModel.onAppearanceDialogShow()
            }
        ),
        ProfileTabItem(
            title = "Privacy",
            icon = Res.drawable.ic_privacy,
            onClick = { /*TODO*/ }
        ),
        ProfileTabItem(
            title = "Help",
            icon = Res.drawable.ic_help,
            onClick = { /*TODO*/ }
        ),
        ProfileTabItem(
            title = "Logout",
            icon = Res.drawable.ic_logout,
            onClick = {
                viewModel.signOut()
            }
        )
    )

    LaunchedEffect(logOutState) {
        if (logOutState.isSuccess) {
            navController.navigate(Screens.AuthNav.route) {
                popUpTo(Screens.HomeNav.route) {
                    inclusive = true
                }
            }
        }
    }

    if (showAppearanceDialog) {
        AppearanceDialog(
            onDismissRequest = { viewModel.onAppearanceDialogDismiss() },
            onThemeChange = {
                localTheme.toggle()
            },
            isSystemInDarkTheme = localTheme.isDark
        )
    }

    if (accountDialogState) {
        AccountDialog(
            onDismissRequest = {
                viewModel.onAccountDialogDismiss()
            },
            onSave = { updatedUser ->
                viewModel.updateProfile(updatedUser)
                viewModel.onAccountDialogDismiss()
            },
            currentUser = profileUserUiState
        )

    }


    Scaffold(
        topBar = {
            ChatAppTopBar(
                title = {
                    Text(text = "Profile", color = MaterialTheme.colorScheme.primary)
                },
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                searchIcon = false
            )
        },
        bottomBar = {
            ChatAppBottomAppBar(navController)
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(80.dp),
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = profileUserUiState.profileImageUrl,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .shadow(elevation = 2.dp),
                            onLoading = {
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "${profileUserUiState.name} ${profileUserUiState.surname}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "+ 90 " + profileUserUiState.phoneNumber?.replace(
                            Regex("(\\d{3})(\\d{3})(\\d{2})(\\d{2})"),
                            "$1 $2 $3 $4"
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.3f.dp)
                    .shadow(elevation = 1.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentPadding = PaddingValues(top = 8.dp),
            ) {
                items(menuItemList) { menuItem ->
                    ProfileMenu(menuIcon = menuItem.icon, menuTitle = menuItem.title, onClick = {
                        menuItem.onClick.invoke()
                    })
                }
            }
        }
        if (uiState.isError) {
            val error = uiState.error
            toastNotificationManager.showNotification(
                notificationData = NotificationData(
                    message = error.toString(),
                    type = NotificationType.Error
                )
            )
        }
        if (updateUserState.isSuccess) {
            toastNotificationManager.showNotification(
                notificationData = NotificationData(
                    message = "Profile updated successfully",
                    type = NotificationType.Success
                )
            )
            viewModel.clearUpdateUserState()
        }
        if (updateUserState.isError) {
            val error = updateUserState.error
            toastNotificationManager.showNotification(
                notificationData = NotificationData(
                    message = error.toString(),
                    type = NotificationType.Warning
                )
            )
            viewModel.clearUpdateUserState()
        }

        ToastNotificationComponent(
            notification = notification,
            toastNotificationManager = toastNotificationManager,
            topPadding = contentPadding.calculateTopPadding() - 10.dp
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}