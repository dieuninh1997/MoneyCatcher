package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.navigation.Screen
import com.ninhttd.moneycatcher.ui.screen.wallet.component.AddWalletButton
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer


@Composable
fun WalletPickerRow(
    wallet: Wallet?,
    onClick: (String) -> Unit,
    onNavigateDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(wallet?.id.toString()) },
        shape = RoundedCornerShape(8.dp),
        color = ColorColdPurplePink
    ) {

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(12.dp))
        if (wallet != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        wallet.name,
                        fontWeight = FontWeight.Bold,
                        color =ColorPinkPrimaryContainer,
                        fontSize = 12.sp
                    )
                    Text("${formatMoney(wallet.balance)}₫", color = Color.White)
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.White
                )

            }
        } else {
            AddWalletButton(onSubmit = {
                onNavigateDetails(Screen.AddWallet.route)
            })
        }
    }
}