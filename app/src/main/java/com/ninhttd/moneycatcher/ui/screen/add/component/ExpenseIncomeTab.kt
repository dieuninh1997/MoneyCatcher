package com.ninhttd.moneycatcher.ui.screen.add.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.common.Constants.EXPENSE_TAB
import com.ninhttd.moneycatcher.common.Constants.INCOME_TAB
import com.ninhttd.moneycatcher.ui.theme.ColorMutedPinkGray
import java.nio.file.WatchEvent

@Composable
fun ExpenseIncomeTab(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(ColorMutedPinkGray),
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        EITabItem(
            title = "Expense",
            isSelected = selectedIndex == EXPENSE_TAB,
            onClick = { onTabSelected(EXPENSE_TAB) },
            icon = Icons.Default.ArrowUpward,
            modifier = Modifier.weight(1f)
        )
        EITabItem(
            title = "Income",
            isSelected = selectedIndex == INCOME_TAB,
            onClick = { onTabSelected(INCOME_TAB) },
            icon = Icons.Default.ArrowDownward,
            modifier = Modifier.weight(1f)
        )
    }
}
