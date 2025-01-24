package com.bekircaglar.bluchat.presentation.message.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.baseline_attach_file_24
import bluchatkmp.composeapp.generated.resources.ic_camera
import bluchatkmp.composeapp.generated.resources.outline_emoji_emotions_24
import com.bekircaglar.bluchat.ui.theme.DarkText
import org.jetbrains.compose.resources.painterResource
import kotlin.math.min


@Composable
fun MessageTextField(
    onSend: (String) -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    onAttachClicked: () -> Unit = {},
    onEmojiClicked: () -> Unit = {},
    onCameraClicked: () -> Unit = {},

    ) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Send,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                onSend(searchText)
            },
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, MaterialTheme.shapes.large)
            .heightIn(min = 50.dp)
            .clip(MaterialTheme.shapes.large),
        placeholder = {
            Text(
                text = placeholderText,
                color = DarkText.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodySmall
            )
        },
        maxLines = 1,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorScheme.primary.copy(alpha = 0.2f),
            focusedContainerColor = colorScheme.primary.copy(alpha = 0.3f),
            focusedTextColor = DarkText,
            unfocusedTextColor = DarkText.copy(alpha = 0.5f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            IconButton(
                onClick = {
                    onEmojiClicked()
                }
            ) {
                Icon(
                    painter = painterResource(resource = Res.drawable.outline_emoji_emotions_24),
                    contentDescription = "Emoji",
                    tint = DarkText,
                    modifier = Modifier
                        .size(25.dp)
                )
            }

        },
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {
                        onAttachClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.baseline_attach_file_24),
                        contentDescription = "Attach",
                        tint = DarkText,
                        modifier = Modifier
                            .size(25.dp)
                    )
                }
                IconButton(
                    onClick = {
                        onCameraClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.ic_camera),
                        contentDescription = "Camera",
                        tint = DarkText,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(25.dp)
                    )
                }

            }
        },
    )
}