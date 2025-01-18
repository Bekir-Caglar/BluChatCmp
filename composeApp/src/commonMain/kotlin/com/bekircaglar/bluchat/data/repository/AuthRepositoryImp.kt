package com.bekircaglar.bluchat.data.repository

import com.bekircaglar.bluchat.domain.model.Users
import com.bekircaglar.bluchat.domain.repository.AuthRepository
import com.bekircaglar.bluchat.utils.QueryState
import com.bekircaglar.bluchat.utils.Response
import com.bekircaglar.bluchat.utils.USERS
import com.mmk.kmpauth.google.GoogleAuthCredentials
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.ChildEvent
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference
) : AuthRepository {


    override suspend fun signIn(email: String, password: String): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            auth.signInWithEmailAndPassword(email, password)
        }.onSuccess {
            emit(QueryState.Success(Unit))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }

    }

    override suspend fun signUp(email: String, password: String): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            auth.createUserWithEmailAndPassword(email, password)
        }.onSuccess {
            emit(QueryState.Success(Unit))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }
    }

    override suspend fun signOut(): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            auth.signOut()
        }.onSuccess {
            emit(QueryState.Success(Unit))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }


    }

    override suspend fun signInWithGoogle(idToken: String): Flow<QueryState<AuthResult>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            val credential = dev.gitlive.firebase.auth.GoogleAuthProvider.credential(
                accessToken = null,
                idToken = idToken
            )

            auth.signInWithCredential(credential)
        }.onSuccess {
            emit(QueryState.Success(it))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }

    }

    override suspend fun checkPhoneNumber(phoneNumber: String): Flow<QueryState<Boolean>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            databaseReference.child(USERS).orderByValue().equalTo(phoneNumber).valueEvents.collect {
                val userExists = it.children.any { userSnapshot ->
                    val user = userSnapshot.value<Users>()
                    user.phoneNumber.toString() == phoneNumber
                }
                if (userExists) {
                    emit(QueryState.Success(true))
                } else {
                    emit(QueryState.Success(false))
                }
            }

        }.onFailure {
            emit(QueryState.Error(it.message))
        }
    }

    override suspend fun createUser(users: Users): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            val currentUserUid = auth.currentUser?.uid
            if (currentUserUid != null) {
                databaseReference.child(USERS).child(currentUserUid)
                    .setValue(users.copy(uid = currentUserUid))
            } else {
                emit(QueryState.Error("User not found"))
            }
        }.onSuccess {
            emit(QueryState.Success(Unit))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }
    }

    override suspend fun deleteUser(): Flow<QueryState<Unit>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            auth.currentUser?.delete()
        }.onSuccess {
            emit(QueryState.Success(Unit))
        }.onFailure {
            emit(QueryState.Error(it.message))
        }
    }

    override suspend fun checkIsUserAlreadyExist(email: String): Flow<QueryState<Boolean>> = flow {
        emit(QueryState.Loading)
        kotlin.runCatching {
            databaseReference.child("Users").valueEvents.collect {
                val userExists = it.children.any { userSnapshot ->
                    val user = userSnapshot.value<Users>()
                    user.email.toString() == email
                }
                if (userExists) {
                    emit(QueryState.Success(true))
                } else {
                    emit(QueryState.Success(false))
                }
            }
        }.onFailure { e ->
            emit(QueryState.Error(e.message))
        }
    }
}