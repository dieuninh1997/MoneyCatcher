package com.ninhttd.moneycatcher.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.others.OthersUiState
import com.ninhttd.moneycatcher.ui.screen.others.OthersViewModel
import com.ninhttd.moneycatcher.ui.screen.others.component.SettingItem
import com.ninhttd.moneycatcher.ui.screen.others.component.SettingsGroup
import com.ninhttd.moneycatcher.ui.screen.others.component.UserInfoSection


@Composable
fun OthersScreen(
    onNavigateDetails: (String) -> Unit,
    viewModel: OthersViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OthersScreen(
        uiState = uiState,
        onNavigateDetails = onNavigateDetails,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OthersScreen(
    uiState: OthersUiState,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {

        //user info
        UserInfoSection()
        Spacer(Modifier.height(24.dp))

        SettingsGroup(
            "Cài đặt giao dịch", listOf(
                SettingItem(
                    "Ví của tôi",
                    Icons.Default.AccountBalanceWallet,
                    onItemClick = { onNavigateDetails(Screen.Wallet.route) }),

//            SettingItem("Thêm thu nhập/chi phí cố định", Icons.Default.Receipt),
//            SettingItem("Chỉnh sửa danh mục", Icons.Default.Edit)
            )
        )
    }
}


@Preview
@Composable
private fun Preview() {
    OthersScreen({})
}