package com.ninhttd.moneycatcher.ui.screen.wallet.add

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ninhttd.moneycatcher.common.hiltActivityViewModel
import com.ninhttd.moneycatcher.di.SessionManager
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.screen.wallet.component.SettingSwitchRow
import com.ninhttd.moneycatcher.ui.screen.editcategory.TopBar
import com.ninhttd.moneycatcher.ui.screen.main.MainSharedViewModel
import com.ninhttd.moneycatcher.ui.screen.wallet.WalletViewModel
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer
import com.ninhttd.moneycatcher.ui.theme.ColorZeroWhite

@Composable
fun AddWalletScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WalletViewModel = hiltViewModel()
) {
    val mainViewModal: MainSharedViewModel = hiltActivityViewModel()
    var walletName by remember { mutableStateOf("") }
    var initialBalance by remember { mutableStateOf("") }
    var isDefault by remember { mutableStateOf(false) }
    var excludeFromTotal by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPinkPrimary)
            .padding(16.dp)
    ) {
        Column () {
            TopBar(title = "Thêm ví", onBackPress = {
                onNavigateUp()
            })

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorColdPurplePink, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                // Nhập tên ví
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                    OutlinedTextField(
                        value = walletName,
                        onValueChange = { walletName = it },
                        placeholder = { Text("Nhập tên ví", color = ColorPinkPrimaryContainer) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Divider(color = ColorPinkPrimary, modifier = Modifier.fillMaxWidth())

                // Switch: Đặt làm ví mặc định
                SettingSwitchRow(
                    icon = Icons.Default.Lock,
                    label = "Đặt làm ví mặc định",
                    checked = isDefault,
                    onCheckedChange = { isDefault = it }
                )

                Divider(color = ColorPinkPrimary, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                // Nhập số dư ban đầu
                Column {
                    Text("Số dư ban đầu", color = ColorPinkPrimaryContainer, fontSize = 14.sp)
                    OutlinedTextField(
                        value = initialBalance,
                        onValueChange = { initialBalance = it },
                        placeholder = { Text("0 đ", color = ColorPinkPrimaryContainer) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            cursorColor = Color.White,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

//        // Switch: Loại trừ khỏi Tổng
            SettingSwitchRow(
                icon = Icons.Default.Close,
                label = "Loại trừ khỏi Tổng",
                checked = excludeFromTotal,
                onCheckedChange = { excludeFromTotal = it }
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Bỏ qua ví này và số dư của nó trong chế độ “Tổng”",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Button(
            onClick = {
                SessionManager.currentUser?.let {
                    val wallet = Wallet(
                        userId = it.id,
                        name = walletName,
                        balance = initialBalance.toLong(),
                        isDefault = isDefault,
                        initBalance = initialBalance.toLong()
                    )
                    mainViewModal.createWallet(wallet) { success ->
                        if(success) {
                            //save prefs

                            onNavigateUp.invoke()
                        } else {
                            Toast.makeText(context, "Error!!", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorColdPurplePink
            ),
        ) {
            Text("Hoàn thành", fontWeight = FontWeight.Bold, color= ColorZeroWhite)
        }
    }
}
