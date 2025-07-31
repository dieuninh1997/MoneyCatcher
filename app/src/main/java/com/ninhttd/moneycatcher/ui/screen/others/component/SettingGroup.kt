package com.ninhttd.moneycatcher.ui.screen.others.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink

data class SettingItem(val title: String, val icon: ImageVector, val onItemClick: () -> Unit)

@Composable
fun SettingsGroup(title: String, items: List<SettingItem>, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title, color = ColorColdPurplePink, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        items.forEach {
            SettingRow(it)
            Spacer(Modifier.height(8.dp))
        }
    }
}