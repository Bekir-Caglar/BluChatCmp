package com.bekircaglar.bluchat.presentation.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_facebook
import com.bekircaglar.bluchat.ui.theme.FacebookBlue
import org.jetbrains.compose.resources.painterResource

@Composable
fun LoginFacebookButton(
    onAuthError: (Exception) -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
) {
    ElevatedButton(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = FacebookBlue,
            contentColor = Color.White
        ),
        modifier = modifier
            .width(350.dp)
            .height(50.dp)
            .background(color = FacebookBlue, shape = MaterialTheme.shapes.medium),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_facebook) ,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                modifier = Modifier,
                fontSize = 16.sp,
                text = buttonText,
                color = Color.White,
            )
        }
    }
}