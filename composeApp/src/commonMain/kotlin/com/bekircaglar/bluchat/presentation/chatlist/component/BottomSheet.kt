package com.bekircaglar.bluchat.presentation.chatlist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import bluchatkmp.composeapp.generated.resources.Res
import bluchatkmp.composeapp.generated.resources.ic_group_chat
import bluchatkmp.composeapp.generated.resources.ic_new_chat
import com.bekircaglar.bluchat.domain.model.SheetOption
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, onClicked: (String) -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    val myList = listOf(
        SheetOption.NewChat,
        SheetOption.CreateGroupChat,
    )


    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        LazyColumn {
            items(myList) { option ->
                Row(Modifier.clickable {
                    onClicked(option.title)
                    onDismiss()
                }, verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.padding(start = 16.dp)) {
                        Image(
                            painter = painterResource(option.icon),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = option.title, color = MaterialTheme.colorScheme.onSurface)
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(16.dp))


            }
        }
    }

}