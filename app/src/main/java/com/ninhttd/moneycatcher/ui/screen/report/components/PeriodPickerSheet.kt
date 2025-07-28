package com.ninhttd.moneycatcher.ui.screen.report.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ninhttd.moneycatcher.common.PeriodType
import com.ninhttd.moneycatcher.ui.theme.ColorColdPurplePink
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimary
import com.ninhttd.moneycatcher.ui.theme.ColorPinkPrimaryContainer
import java.time.LocalDate
import java.time.temporal.WeekFields

@Composable
fun PeriodPickerSheet(
    currentState: PeriodFilterState,
    onDismiss: () -> Unit,
    onConfirm: (PeriodFilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentYear = LocalDate.now().year
    var selectedType by remember { mutableStateOf(currentState.type) }
    var selectedMap by remember {
        mutableStateOf(
            mapOf(
                PeriodType.WEEK to "Tuần ${LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear())}",
                PeriodType.MONTH to "Tháng ${LocalDate.now().monthValue}",
                PeriodType.YEAR to "${LocalDate.now().year}"
            )
        )
    }
    var selectedValue by remember { mutableStateOf(selectedMap[selectedType] ?: "") }

    LaunchedEffect(currentState) {
        selectedType = currentState.type
        selectedMap = selectedMap.toMutableMap().apply {
            put(selectedType, currentState.value)
        }
    }

    LaunchedEffect(selectedType, selectedMap) {
        selectedValue = selectedMap[selectedType] ?: ""
    }

    val values = when (selectedType) {
        PeriodType.WEEK -> (1..53).map { "Tuần $it" }
        PeriodType.MONTH -> (1..12).map { "Tháng $it" }
        PeriodType.YEAR -> ((currentYear - 10)..currentYear).map { it.toString() }.reversed()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = ColorPinkPrimary
    ) {
        BoxWithConstraints {
            val maxHeight = this.maxHeight
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight * 0.9f), // giới hạn chiều cao
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Chọn loại thời gian",
                        fontWeight = FontWeight.Bold,
                        color = ColorColdPurplePink
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PeriodType.entries.forEach {
                            val isSelected = it == selectedType
                            Button(
                                onClick = {
                                    selectedType = it
//                                    selectedValue = when (it) {
//                                        PeriodType.WEEK -> "Tuần ${
//                                            LocalDate.now()
//                                                .get(WeekFields.ISO.weekOfWeekBasedYear())
//                                        }"
//
//                                        PeriodType.MONTH -> "Tháng ${LocalDate.now().monthValue}"
//                                        PeriodType.YEAR -> "${LocalDate.now().year}"
//                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) ColorPinkPrimaryContainer else Color.LightGray
                                )
                            ) {
                                Text(it.displayName, color = Color.White)
                            }
                        }
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(values.size) { valueIndex ->
                            val value = values[valueIndex]
                            val isSelected = selectedValue == value
                            val backgroundColor = if (isSelected) {
                                ColorPinkPrimaryContainer
                            } else {
                                Color.LightGray
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(backgroundColor)
                                    .clickable {
                                        selectedValue = value
                                        selectedMap = selectedMap.toMutableMap().apply {
                                            put(selectedType, value)
                                        }
                                    }
                                    .padding(vertical = 16.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value,
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = "Hủy",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        TextButton(onClick = {
                            onConfirm(PeriodFilterState.forSelection(selectedType, selectedMap[selectedType] ?: ""))
                        }) {
                            Text(
                                text = "Xác nhận",
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

            }
        }
    }
}
