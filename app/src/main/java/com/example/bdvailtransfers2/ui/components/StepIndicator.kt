package com.example.bdvailtransfers2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Индикатор шагов вверху экрана New Booking (3 полосы, как на макете).
 */
@Composable
fun StepIndicator(
    currentStep: Int,
    stepsCount: Int = 3,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 0.dp, vertical = 8.dp)
    ) {
        repeat(stepsCount) { index ->
            val isActive = index + 1 <= currentStep
            val color: Color = when {
                isActive && index + 1 == currentStep ->
                    MaterialTheme.colorScheme.primary
                isActive ->
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                else ->
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
            }

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(color, RoundedCornerShape(999.dp))
            )

            if (index != stepsCount - 1) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}
