package com.ninhttd.moneycatcher.ui.screen.wallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPositiveGreen

@Composable
fun AddWalletButton(modifier: Modifier = Modifier, onSubmit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorColdPurplePink, RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable { onSubmit() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AddCircle, contentDescription = null, tint = ColorPositiveGreen)
        Spacer(Modifier.width(12.dp))
        Text("Thêm ví", color = Color.White)
    }
}