package com.bekircaglar.bluchat.presentation.message.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_send
import coil3.Uri
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSendBottomSheet(
    imageResId: Uri,
    onSend: (img:String,txt:String) -> Unit,
    onDismiss: () -> Unit,
) {
    var message by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = imageResId,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MessageTextField(
                            searchText = message,
                            onSearchTextChange = { newText ->
                                message = newText.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                            },
                            onSend = {
                                onSend(imageResId.toString(),message)

                            },
                            placeholderText = "Add a caption...",
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(resource = Res.drawable.ic_send),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    onSend(imageResId.toString(),message)
                                }
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismiss
    )
}