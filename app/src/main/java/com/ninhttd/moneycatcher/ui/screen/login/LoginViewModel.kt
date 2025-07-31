package com.ninhttd.moneycatcher.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.di.AppPreferencesManager
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.domain.model.UserInfo
import com.ninhttd.moneycatcher.domain.usecase.LoginUseCase
import com.ninhttd.moneycatcher.domain.usecase.LogoutUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUsecase,
    private val appPrefs: AppPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _logoutState = MutableStateFlow<LogoutUiState>(LogoutUiState.Idle)
    val logoutState: StateFlow<LogoutUiState> = _logoutState

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
                val user = result.getOrNull()
                if (user != null) {
                    SessionManager.login(user, appPrefs)
                }

                LoginUiState.Success(result)
            } else {
                LoginUiState.Error(result.exceptionOrNull()?.message ?: "unknow error")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = LogoutUiState.Loading
            val result = logoutUseCase()
            _logoutState.value = if (result.isSuccess) {
                SessionManager.logout(appPrefs)
                LogoutUiState.Success
            } else {
                LogoutUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

}