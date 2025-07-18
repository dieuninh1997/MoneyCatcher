package com.ninhttd.moneycatcher.ui.screen.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.component.WalletPickerRow
import com.ninhttd.moneycatcher.ui.screen.editcategory.TopBar
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.wallet.component.AddWalletButton
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary

@Composable
fun WalletScreen(
    onNavigateDetails: (String) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWalletId by mainViewModal.currentWalletId.collectAsState(initial = null)
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPinkPrimary)
            .padding(16.dp)
    ) {
        // AppBar
        TopBar(title = "Ví của tôi", onBackPress = { onNavigateUp.invoke() })

        Spacer(Modifier.height(24.dp))

        // Tổng số dư
//        TotalBalanceCard(amount = "24,676,000đ")

        Spacer(Modifier.height(16.dp))

//        Text("Đã bao gồm trong tổng", color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(8.dp))

        // Ví mặc định
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(walletList.size) { index ->
                val wallet = walletList[index]
                WalletPickerRow(
                    wallet = wallet,
                    { walletId ->
                        onNavigateDetails("${Screen.WalletDetail.route}/$walletId")
                    },
                    onNavigateDetails = {
                        onNavigateDetails(Screen.AddWallet.route)
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Thêm ví
        AddWalletButton(onSubmit = {
            onNavigateDetails(Screen.AddWallet.route)
        })
    }
}


