package com.ninhttd.moneycatcher.ui.screen.wallet.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ehsanmsz.mszprogressindicator.progressindicator.BallPulseSyncProgressIndicator
import com.ninhttd.moneycatcher.ui.theme.ColorOnSurfaceVariantDark

@Composable
fun LoadingBubble(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = ColorOnSurfaceVariantDark, // màu nền giống trong ảnh
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BallPulseSyncProgressIndicator(
            color = Color.White,
            animationDuration = 800,
            animationDelay = 200,
            ballCount = 3,
            ballDiameter = 8.dp
        )
    }

}