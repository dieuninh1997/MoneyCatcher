package com.ninhttd.moneycatcher.ui.screen.calendar.component

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties

data class BarData(
    val label: String,
    val value: Float, // giá trị dương/âm
    val color: Color
)

@SuppressLint("DefaultLocale")
fun formatCompactToDouble(value: Double): Double {
    val abs = kotlin.math.abs(value)
    return when {
        abs >= 1_000_000 -> value / 1_000_000
        abs >= 1_000 -> value / 1_000
        else -> value
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun BarChartView(data: List<Bars>, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    ColumnChart(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .width(screenWidth)
            .height(300.dp),
        data = data,
        labelHelperProperties = LabelHelperProperties(
            enabled = false,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White)
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White)
        ),labelProperties= LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(color = Color.White)
        )
    )
}
