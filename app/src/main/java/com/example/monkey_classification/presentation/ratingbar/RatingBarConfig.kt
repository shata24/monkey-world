package com.example.monkey_classification.presentation.ratingbar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.monkey_classification.ui.theme.primaryColor

class RatingBarConfig {
    var size: Dp = 40.dp
        private set
    var padding: Dp = 2.dp
        private set
    var style: RatingBarStyle = RatingBarStyle.Normal
        private set
    var numStars: Int = 5
        private set
    var isIndicator: Boolean = false
        private set
    var activeColor: Color = primaryColor
        private set
    var inactiveColor: Color = primaryColor.copy(alpha = 0.5f)
        private set
    var stepSize: StepSize = StepSize.ONE
        private set
    var hideInactiveStars: Boolean = false
        private set
    fun style(value: RatingBarStyle): RatingBarConfig =
        apply { style = value }
}