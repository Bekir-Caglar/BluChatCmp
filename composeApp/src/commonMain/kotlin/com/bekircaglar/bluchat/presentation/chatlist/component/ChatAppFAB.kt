package com.bekircaglar.bluchat.presentation.chatlist.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChatAppFAB(
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = Color.White,
    onClick:() -> Unit,
    size: Dp = 56.dp,
    cornerRadius: Dp = 12.dp
) {
    FloatingActionButton(
        onClick = {onClick() },
        shape = RoundedCornerShape(cornerRadius),
        containerColor = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.size(size)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(size / 2),
            tint = contentColor
        )
    }
}