package com.bekircaglar.bluchat.presentation.auth.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthButton(
    onClick: () -> Unit,
    buttonIcon: Painter? = null,
    buttonText: String,
    enabled : Boolean = true,
    buttonTextSize: TextUnit = 16.sp,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    ElevatedButton(
        onClick = { onClick() },
        modifier = Modifier
            .width(350.dp)
            .height(50.dp),
        shape = ShapeDefaults.Medium,
        colors = ButtonColors(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContainerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.background
        ),
        enabled = enabled
    ) {
        if (buttonIcon != null) {
            Icon(
                painter = buttonIcon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        }

        Text(text = buttonText, fontSize = buttonTextSize)

    }
}