package com.bekircaglar.bluchat.presentation.chatlist.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bekircaglar.bluchat.domain.model.Chats
import com.bekircaglar.bluchat.domain.model.Users
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatBottomSheet(
    textFieldValue: String,
    onSearchQueryChange: (String) -> Unit,
    searchResults: List<Users>,
    onDismiss: () -> Unit,
    onItemClick: (Users) -> Unit,
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Select contact to chat",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SearchTextField(
                query = textFieldValue,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                text = "Search by phone number"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(searchResults, key = { it.email!! }) { contact ->
                    val myChat = Chats(
                        chatRoomId = "",
                        chatImage = contact.profileImageUrl!!,
                        name = contact.name!!,
                        surname = contact.surname!!,
                        isOnline = contact.status!!
                    )
                    ChatElement(
                        chats = myChat,
                        onClick = {
                            onItemClick(contact)
                            coroutineScope.launch {
                                bottomSheetState.hide()
                            }
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
