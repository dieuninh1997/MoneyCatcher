package com.ninhttd.moneycatcher.ui.screen.wallet.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.screen.editcategory.TopBar
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.wallet.WalletViewModel
import com.ninhttd.moneycatcher.ui.screen.wallet.component.SettingSwitchRow
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorOnSurfaceVariantDark
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer

@Composable
fun WalletDetailScreen(
    walletId: String,
    onNavigateUp: () -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    val walletList by mainViewModal.walletList.collectAsState()
    val currentWallet by mainViewModal.currentWallet.collectAsState(initial = null)

    var selectedWallet by remember { mutableStateOf(currentWallet) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val currentUser = remember { SessionManager.currentUser }

    LaunchedEffect(walletList) {
        if (walletList.isNotEmpty() && currentWallet == null) {
            selectedWallet = walletList.find { it.id == walletId }
            mainViewModal.setCurrentWalletId(selectedWallet?.id.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(ColorPinkPrimary)
            .padding(16.dp)
    ) {
        TopBar(title = "Chi tiết ví", onBackPress = {
            onNavigateUp()
        })

        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ColorColdPurplePink),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        selectedWallet?.name ?: "",
                        color = Color.White
                    )
                    Text(
                        "${formatMoney(selectedWallet?.balance ?: 0)}₫",
                        color = ColorPinkPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Divider(color = ColorPinkPrimary, modifier = Modifier.fillMaxWidth())
            SettingSwitchRow(
                icon = Icons.Default.AccountBalanceWallet,
                label = "Đặt làm ví mặc định",
                checked = selectedWallet?.isDefault == true,
                onCheckedChange = {
                    mainViewModal.setWalletAsDefault(currentWallet?.id.toString())
                }
            )
            Divider(color = ColorPinkPrimary, modifier = Modifier.fillMaxWidth())
            // Số dư ban đầu
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Số dư ban đầu", color = Color.Gray)
                Text(
                    "${formatMoney(selectedWallet?.initBalance ?: 0)}₫",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Người dùng đang sử dụng ví", color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(ColorColdPurplePink, RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.LightGray)
            Spacer(modifier = Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text("Người ẩn danh", color = Color.White)
                Text(currentUser?.email ?: "", color = Color.Gray, fontSize = 13.sp)
            }
            Box(
                Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorMutedPinkGray)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("Chủ sở hữu", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // Điều chỉnh số dư
        ActionRow(
            icon = Icons.Default.Edit,
            title = "Điều chỉnh số dư hiện tại",
            onClick = {
//                onNavigateDetails(Screen.)
            }
        )

        Spacer(Modifier.height(24.dp))

        // Xóa ví
        Text(
            "Xóa ví",
            color = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    showConfirmDialog = true
                }
                .padding(8.dp)
        )

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Xác nhận xóa ví") },
                text = { Text("Bạn có chắc chắn muốn xóa ví này không? Hành động này không thể hoàn tác!") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                        mainViewModal.deleteWallet(walletId) // hoặc logic xoá của bạn
                        onNavigateUp()
                    }) {
                        Text("Xoá", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Huỷ")
                    }
                }
            )
        }
    }
}


@Composable
fun ActionRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(ColorColdPurplePink, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, color = Color.White)
    }
}