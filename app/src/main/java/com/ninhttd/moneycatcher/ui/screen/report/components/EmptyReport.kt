package com.ninhttd.moneycatcher.ui.screen.report.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.R

@Composable
fun EmptyReport(
    modifier: Modifier = Modifier,
    onAddTransactionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Ảnh hộp rỗng
        Image(
            painter = painterResource(id = R.drawable.ic_empty_box), // bạn thêm icon này vào drawable
            contentDescription = "Empty",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tiêu đề
        Text(
            text = "Báo cáo trống",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mô tả
        Text(
            text = "Không có giao dịch nào trong khoảng thời gian này.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button
        Button(
            onClick = onAddTransactionClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Nhập giao dịch")
        }
    }
}
