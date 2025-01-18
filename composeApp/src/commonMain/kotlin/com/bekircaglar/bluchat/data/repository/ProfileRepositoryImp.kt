package com.bekircaglar.bluchat.data.repository

import coil3.Uri
import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.ProfileRepository
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.USERS
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileRepositoryImp(
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : ProfileRepository {

    override suspend fun getCurrentUser(): Flow<QueryState<Users>> = flow {
        emit(QueryState.Loading)

        val user = auth.currentUser

        if (user != null) {
            databaseReference.child(USERS).valueEvents.collect { dataSnapshot ->
                val currentUser = dataSnapshot.children.find { userSnapshot ->
                    userSnapshot.key == user.uid
                }?.value<Users?>()
                if (currentUser != null) {
                    emit(QueryState.Success(currentUser))
                } else {
                    emit(QueryState.Error("User not found"))
                }
            }


        } else {
            emit(QueryState.Error("User not found"))
        }
    }

    override suspend fun uploadImage(uri: Uri): Flow<QueryState<Uri>> = flow{

    }

}