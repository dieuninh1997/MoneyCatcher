package com.ninhttd.moneycatcher.common

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> activityViewModel(): VM {
    val activity = LocalContext.current as ComponentActivity
    return viewModel(viewModelStoreOwner = activity)
}

@Composable
inline fun <reified VM : ViewModel> hiltActivityViewModel(): VM {
    val activity = LocalContext.current as ViewModelStoreOwner
    return hiltViewModel(viewModelStoreOwner = activity)
}