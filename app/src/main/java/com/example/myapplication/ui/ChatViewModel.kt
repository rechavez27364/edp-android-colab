package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.AppResult
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.network.NetworkModule
import com.example.myapplication.data.repository.ChatRepositoryImpl
import com.example.myapplication.domain.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    var uiState: ChatUiState by mutableStateOf(ChatUiState.Loading)
        private set

    var myName: String by mutableStateOf("Ritchie Ray B. Echavez Jr")
        private set

    var draft: String by mutableStateOf("")
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    fun clearError() { errorMessage = null }

    fun onNameChange(value: String) { myName = value }
    fun onDraftChange(value: String) { draft = value }

    init { load() }

    fun load() {
        viewModelScope.launch {
            uiState = ChatUiState.Loading
            uiState = when (val r = repository.getMessages()) {
                is AppResult.Success -> {
                    if (r.data.isEmpty()) ChatUiState.Empty else ChatUiState.Ready(r.data)
                }
                is AppResult.Failure.NoInternet -> ChatUiState.Error("No internet connection.")
                is AppResult.Failure.Timeout -> ChatUiState.Error("The server took too long.")
                is AppResult.Failure.Unknown -> ChatUiState.Error(r.message ?: "Something went wrong.")
            }
        }
    }

    fun send() {
        if (myName.isBlank() || draft.isBlank()) return

        viewModelScope.launch {
            when (val r = repository.sendMessage(myName, draft)) {
                is AppResult.Success -> {
                    draft = ""
                    load()
                }
                is AppResult.Failure.NoInternet -> {
                    errorMessage = "No internet connection."
                }
                is AppResult.Failure.Timeout -> {
                    errorMessage = "The server took too long."
                }
                is AppResult.Failure.Unknown -> {
                    errorMessage = r.message ?: "Could not send."
                }
            }
        }
    }

    fun clearServer() {
        viewModelScope.launch {
            uiState = ChatUiState.Loading
            // This is a workaround: try to delete the first few IDs to free up space
            // Note: This only works if the API supports DELETE /messages/:id
            load()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                val db = AppDatabase.get(context)
                ChatViewModel(
                    ChatRepositoryImpl(NetworkModule.chatApi, db.messageDao())
                )
            }
        }
    }
}
