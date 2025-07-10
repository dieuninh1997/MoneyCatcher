package com.ninhttd.moneycatcher.ui.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.ui.screen.add.component.formatMoney
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import com.ninhttd.moneycatcher.ui.theme.ColorOnSurfaceVariantDark


@Composable
fun TopSpendingItem(
    icon: String,
    title: String,
    amount: Long,
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(ColorMutedPinkGray, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Medium)
                Text(
                    "${formatMoney(amount)}₫ (${String.format("%.2f", percentage)}%)",
                    color = ColorOnSurfaceVariantDark,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = percentage / 100f,
            color = Color(0xFF42A5F5),
            backgroundColor = ColorOnSurfaceVariantDark,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
    }
}