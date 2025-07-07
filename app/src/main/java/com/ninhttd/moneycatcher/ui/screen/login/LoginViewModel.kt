package com.ninhttd.moneycatcher.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val appPrefs: AppPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    var userState by mutableStateOf<UserInfo?>(null)

    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        //TODO
    }

    fun reset() {
        _uiState.value = LoginUiState.Idle
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val result = loginUseCase(email, password)
            userState = result.getOrNull()
            SessionManager.currentUser = userState
            _uiState.value = if (result.isSuccess) {
                LoginUiState.Success(result)
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "unknow error")
            }
        }
    }


}