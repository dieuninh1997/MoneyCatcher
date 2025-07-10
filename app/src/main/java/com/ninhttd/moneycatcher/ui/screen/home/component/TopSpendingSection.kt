package com.ninhttd.moneycatcher.ui.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.common.TimeFilter
import com.ninhttd.moneycatcher.domain.model.CategorySummary
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink

@Composable
fun TopSpendingSection(
    topCategories: List<CategorySummary>,
    timeFilter: TimeFilter,
    onFilterClick: (TimeFilter) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Chi tiêu hàng đầu", fontWeight = FontWeight.Bold, color = ColorColdPurplePink)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onViewAllClick) {
                Text("Xem tất cả", color = ColorColdPurplePink)
            }
        }

        Row(Modifier.padding(bottom = 8.dp)) {
            listOf(TimeFilter.WEEKLY, TimeFilter.MONTHLY, TimeFilter.YEARLY).forEach {
                FilterChip(
                    text = it.name.replaceFirstChar(Char::uppercase),
                    selected = it == timeFilter,
                    onClick = { onFilterClick(it) }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Column {
            topCategories.forEach {
                TopSpendingItem(
                    icon = it.category.icon,
                    title = it.category.name,
                    amount = it.amount,
                    percentage = it.percent
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

