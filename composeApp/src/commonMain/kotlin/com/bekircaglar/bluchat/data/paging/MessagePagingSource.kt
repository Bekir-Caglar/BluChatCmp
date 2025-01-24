package com.bekircaglar.bluchat.data.repository

import com.bekircaglar.bluchat.domain.model.message.Message
import com.bekircaglar.bluchat.utils.MESSAGE_COLLECTION
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.utils.STORED_MESSAGES
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.callbackFlow as callbackFlow1

class FirebaseDataSource(private val database: DatabaseReference) {

    fun getInitialMessages(chatId: String): Flow<QueryState<List<Message>>> = flow {
        val messagesRef = database.child(MESSAGE_COLLECTION).child(chatId).child(STORED_MESSAGES)

        messagesRef.orderByKey().limitToLast(15).valueEvents.collect {
            val messages = it.children.map { it.value<Message>() }
            emit(QueryState.Success(messages))
        }
    }

    fun getMoreMessages(chatId: String, lastKey: String): Flow<Response<List<Message>>> = flow {
        val messagesRef = database.child(MESSAGE_COLLECTION).child(chatId).child(
            STORED_MESSAGES
        )
        messagesRef.orderByKey().endAt(lastKey).limitToLast(16).valueEvents.collect {
            val messages = it.children.map { it.value<Message>() }
            emit(Response.Success(messages))
        }

    }
}