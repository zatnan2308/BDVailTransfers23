package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bdvailtransfers2.NavRoutes
import com.example.bdvailtransfers2.data.local.UserPreferences
import com.example.bdvailtransfers2.data.local.UserPreferencesManager
import com.example.bdvailtransfers2.data.model.Booking
import com.example.bdvailtransfers2.ui.theme.BDVailPrimary
import com.example.bdvailtransfers2.ui.theme.ChipBackground

/**
 * Главный экран (Home) в стиле макета:
 * - "Hi, Guest!"
 * - синяя карточка Upcoming Trip
 * - блок AI Concierge
 * - две большие плитки: New Booking / My Trips
 */
@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userPreferencesManager = remember { UserPreferencesManager(context) }
    val userPrefs by userPreferencesManager.userPreferencesFlow.collectAsState(
        initial = UserPreferences()
    )

    val name = userPrefs.name.takeIf { it.isNotBlank() } ?: "Guest"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Заголовок + иконка профиля
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hi, $name!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Where are we going today?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }

                    // Круглая иконка профиля (пока эмодзи-заглушка)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👤",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Синяя карточка с ближайшей поездкой
            // Сейчас – примерные данные, позже можно подставить реальный Booking
            item {
                UpcomingTripCard(
                    booking = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Блок AI Concierge
            item {
                AiConciergeCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // сюда позже можно повесить навигацию на отдельный экран
                    }
                )
            }

            // Две крупные плитки: New Booking и My Trips
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HomeActionTile(
                        title = "New Booking",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(NavRoutes.BOOKING_FORM) }
                    )
                    HomeActionTile(
                        title = "My Trips",
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(NavRoutes.MY_BOOKINGS) }
                    )
                }
            }
        }
    }
}

/**
 * Синяя карточка "Upcoming Trip" как на макете.
 * Поддерживает и реальный booking, и заглушку.
 */
@Composable
private fun UpcomingTripCard(
    booking: Booking?,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            BDVailPrimary,
            BDVailPrimary.copy(alpha = 0.85f)
        )
    )

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Upcoming Trip",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                val from = booking?.pickupLocation ?: "Denver Airport (DEN)"
                val to = booking?.dropoffLocation ?: "Vail, CO"
                val date = booking?.date ?: "2023-11-25"
                val time = booking?.time ?: "14:30"
                val status = booking?.status ?: "confirmed"

                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = from,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "To",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = to,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    BookingStatusChipOnBlue(status = status)
                }
            }
        }
    }
}

/**
 * Маленький чип статуса на синей карточке.
 */
@Composable
private fun BookingStatusChipOnBlue(status: String) {
    val label = when (status.lowercase()) {
        "confirmed" -> "Confirmed"
        "pending" -> "Pending"
        "completed" -> "Completed"
        "cancelled" -> "Cancelled"
        else -> status
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

/**
 * Карточка AI Concierge (светлая кремовая).
 */
@Composable
private fun AiConciergeCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF5E5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "AI Concierge",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ask about weather & dining.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Круглый акцент справа
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "›", color = BDVailPrimary)
            }
        }
    }
}

/**
 * Плитка-действие для New Booking / My Trips.
 */
@Composable
private fun HomeActionTile(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Круглый голубой значок
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = BDVailPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
