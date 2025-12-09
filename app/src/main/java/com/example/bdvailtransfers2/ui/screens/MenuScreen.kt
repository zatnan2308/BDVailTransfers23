package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // включает width, height, padding и т.д.
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bdvailtransfers2.ui.theme.BDVailPrimary

/**
 * Экран Menu в стиле макета:
 * - Settings
 * - Support
 * - Legal Documents
 * - блок "Visit our website bdvail.com".
 */
@Composable
fun MenuScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Заголовок
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Список пунктов меню
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MenuItemRow(
                    iconText = "⚙️",
                    title = "Settings",
                    onClick = {
                        // позже можно вызвать navController.navigate(...)
                    }
                )
                MenuItemRow(
                    iconText = "📞",
                    title = "Support",
                    onClick = {
                        // например navController.navigate(NavRoutes.SUPPORT)
                    }
                )
                MenuItemRow(
                    iconText = "🛡️",
                    title = "Legal Documents",
                    onClick = {
                        // navController.navigate(NavRoutes.LEGAL) — добавим позже
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Блок "Need more help? Visit our website..."
            HelpWebsiteCard(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Одна строка меню как на макете.
 */
@Composable
private fun MenuItemRow(
    iconText: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка слева
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFFE0EBFF),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Текст пункта
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            // Маленькая стрелка справа
            Text(
                text = "›",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

/**
 * Нижний блок "Need more help? Visit our website bdvail.com".
 */
@Composable
private fun HelpWebsiteCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Need more help?",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Visit our website bdvail.com",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = BDVailPrimary
            )
        }
    }
}
