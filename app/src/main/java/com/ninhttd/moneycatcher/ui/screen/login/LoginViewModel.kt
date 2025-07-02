package com.ninhttd.moneycatcher.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninhttd.moneycatcher.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState


    init {
        initialiseUiState()
    }

    private fun initialiseUiState() {
        //TODO
    }

    fun login(email: String, password: String) {

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val res = loginUseCase(email, password)
            _uiState.value = if (res.isSuccess) {
                LoginUiState.Success(res.getOrNull()!!)
            } else {
                LoginUiState.Error(res.exceptionOrNull()?.message ?: "unknow error")
            }
        }
    }
}