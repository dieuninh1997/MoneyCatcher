package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.domain.model.Wallet
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer
import com.ninhttd.moneycatcher.ui.theme.ColorSurfaceDark
import com.ninhttd.moneycatcher.ui.theme.ColorZeroWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletPickerBottomSheet(
    currentWalletId: String,
    wallets: List<Wallet>,
    onDismiss: () -> Unit,
    onSelect: (Wallet) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ColorSurfaceDark,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Chọn Ví", style = MaterialTheme.typography.titleMedium, color = ColorZeroWhite)

            Spacer(modifier = Modifier.height(12.dp))

            wallets.forEach { wallet ->
                WalletItem(
                    wallet = wallet,
                    isSelected = wallet.id == currentWalletId,
                    onClick = { onSelect(wallet) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun WalletItem(
    wallet: Wallet,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderModifier = if (isSelected) {
        Modifier.border(
            width = 2.dp,
            color = ColorPinkPrimary,
            shape = RoundedCornerShape(12.dp)
        )
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .then(borderModifier)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ColorPinkPrimaryContainer)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            tint = if (isSelected) ColorColdPurplePink else ColorPinkPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                wallet.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) ColorColdPurplePink else ColorPinkPrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "${formatMoney(wallet.balance)}VND",
                color = if (isSelected) ColorColdPurplePink else ColorPinkPrimary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = ColorPinkPrimary,
                        shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Selected",
                    tint = ColorColdPurplePink,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}