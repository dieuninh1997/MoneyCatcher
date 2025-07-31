package com.ninhttd.moneycatcher.ui.screen.add

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.domain.model.UserInfo
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.login.LoginViewModel
import com.ninhttd.moneycatcher.ui.screen.login.LogoutUiState
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.others.OthersUiState
import com.ninhttd.moneycatcher.ui.screen.others.OthersViewModel
import com.ninhttd.moneycatcher.ui.screen.others.component.SettingItem
import com.ninhttd.moneycatcher.ui.screen.others.component.SettingsGroup
import com.ninhttd.moneycatcher.ui.screen.others.component.UserInfoSection
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary


@Composable
fun OthersScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    viewModel: OthersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showDialog by viewModel.showLogoutConfirmDialog
    val showInfoAppDialog by viewModel.showInfoAppDialog

    val loginViewModal: LoginViewModel = hiltActivityViewModel()
    val logoutState by loginViewModal.logoutState.collectAsState()

    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val currentUser by mainViewModal.currentUser.collectAsState()
    val context = LocalContext.current
    val (appName, versionName, versionCode) = remember { getAppInfo(context) }

    when (logoutState) {
        is LogoutUiState.Success -> {
            // 👇 Navigate về màn login và xóa backstack
            LaunchedEffect(Unit) {
                onNavigateToLogin()
            }
        }

        is LogoutUiState.Error -> {
            val message = (logoutState as LogoutUiState.Error).message
            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
        }

        else -> Unit
    }
    OthersScreen(
        uiState = uiState,
        currentUser = currentUser,
        onNavigateDetails = onNavigateDetails,
        onLogout = {
            viewModel.onLogoutClicked()
        },
        onInfoApp = {
            viewModel.onInfoAppClicked()
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLogoutDialog() },
            title = { Text("Xác nhận đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        loginViewModal.logout()
                        viewModel.dismissLogoutDialog()
                    }
                ) {
                    Text("Đăng xuất")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissLogoutDialog() }) {
                    Text("Hủy")
                }
            }
        )
    }

    if (showInfoAppDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissInfoAppDialog() },
            title = { Text(appName) },
            text = { Text("Phiên bản $versionName ($versionCode)") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissInfoAppDialog()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun getAppInfo(context: Context): Triple<String, String, Int> {
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    val appName = packageManager.getApplicationLabel(
        packageManager.getApplicationInfo(packageName, 0)
    ).toString()
    val versionName = packageInfo.versionName ?: "Unknown"
    val versionCode = packageInfo.longVersionCode.toInt() // dùng longVersionCode cho API 28+

    return Triple(appName, versionName, versionCode)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OthersScreen(
    uiState: OthersUiState,
    currentUser: UserInfo?,
    onNavigateDetails: (String) -> Unit,
    onLogout: () -> Unit,
    onInfoApp: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPinkPrimary)
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        //user info
        currentUser?.let {
            UserInfoSection(it)
        }
        Spacer(Modifier.height(24.dp))

        SettingsGroup(
            "Cài đặt giao dịch", listOf(
                SettingItem(
                    "Ví của tôi",
                    Icons.Default.AccountBalanceWallet,
                    onItemClick = { onNavigateDetails(Screen.Wallet.route) }),
                SettingItem(
                    "Chỉnh sửa danh mục",
                    Icons.Default.Edit,
                    onItemClick = {
                        onNavigateDetails(Screen.EditCategory.route)
                    })
            )
        )

        SettingsGroup(
            "Khác", listOf(
                SettingItem(
                    "Thông tin ứng dụng",
                    Icons.Default.Info,
                    onItemClick = {
                        onInfoApp()
                    }),
                SettingItem(
                    "Đăng xuất",
                    Icons.AutoMirrored.Filled.Logout,
                    onItemClick = {
                        onLogout()
                    })
            )
        )
    }
}


//@Preview
//@Composable
//private fun Preview() {
//    OthersScreen({})
//}