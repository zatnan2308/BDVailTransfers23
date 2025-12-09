package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // width, height, padding, size и т.д.
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bdvailtransfers2.ui.components.BDButton
import com.example.bdvailtransfers2.ui.components.BDTextField
import com.example.bdvailtransfers2.ui.theme.BDVailPrimary

/**
 * Экран Support в стиле макета:
 * - Call us 24/7
 * - Email us
 * - Send a message (textarea + голубая кнопка).
 */
@Composable
fun SupportScreen() {
    var messageText by remember { mutableStateOf("") }

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

            // Заголовок, как на скрине
            Text(
                text = "Support",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            // Карточка "Call us 24/7"
            SupportContactCard(
                iconText = "📞",
                title = "CALL US 24/7",
                mainLine = "+1 (970) 555-0123",
                modifier = Modifier.fillMaxWidth()
            )

            // Карточка "Email us"
            SupportContactCard(
                iconText = "✉️",
                title = "EMAIL US",
                mainLine = "support@bdvail.com",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Заголовок "Send a Message"
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = BDVailPrimary.copy(alpha = 0.06f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💬",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Send a Message",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Многострочное поле "Describe your issue..."
            BDTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Голубая кнопка "Send Message"
            BDButton(
                text = "Send Message",
                onClick = {
                    // На отдельном этапе повесим реальную отправку сообщения в API
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Карточка контакта (Call / Email) как на макете.
 */
@Composable
private fun SupportContactCard(
    iconText: String,
    title: String,
    mainLine: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F7FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Круглый голубой значок
            Box(
                modifier = Modifier
                    .size(40.dp)
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

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = BDVailPrimary.copy(alpha = 0.8f)
                )
                Text(
                    text = mainLine,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
