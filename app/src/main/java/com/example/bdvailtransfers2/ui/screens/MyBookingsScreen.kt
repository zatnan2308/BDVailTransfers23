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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bdvailtransfers2.data.local.UserPreferences
import com.example.bdvailtransfers2.data.local.UserPreferencesManager
import com.example.bdvailtransfers2.data.model.Booking
import com.example.bdvailtransfers2.ui.theme.StatusCancelled
import com.example.bdvailtransfers2.ui.theme.StatusConfirmed
import com.example.bdvailtransfers2.ui.theme.StatusPending
import com.example.bdvailtransfers2.ui.viewmodel.MyBookingsUiState
import com.example.bdvailtransfers2.ui.viewmodel.MyBookingsViewModel

/**
 * Экран "My Trips" как на макете:
 * - Табы Upcoming / History
 * - Карточки поездок с цветными статусами.
 *
 * Брони подтягиваются по телефону из UserPreferences (если он сохранён).
 */
@Composable
fun MyBookingsScreen(
    viewModel: MyBookingsViewModel = viewModel()
) {
    val uiState: MyBookingsUiState by viewModel.uiState.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    val userPrefsManager = remember { UserPreferencesManager(context) }
    val userPrefs by userPrefsManager.userPreferencesFlow.collectAsState(
        initial = UserPreferences()
    )

    // Выбранный таб
    var selectedTab by remember { mutableStateOf(MyTripsTab.Upcoming) }

    // Авто-загрузка по сохранённому телефону
    LaunchedEffect(userPrefs.phone) {
        val phone = userPrefs.phone
        if (phone.isNotBlank() && uiState.bookings.isEmpty() && !uiState.isLoading) {
            viewModel.loadBookings(phone)
        }
    }

    // Группировка по статусам — просто и надёжно
    val upcomingBookings = uiState.bookings.filter { booking ->
        when (booking.status.lowercase()) {
            "pending", "confirmed" -> true
            else -> false
        }
    }

    val historyBookings = uiState.bookings.filter { booking ->
        when (booking.status.lowercase()) {
            "completed", "cancelled" -> true
            else -> false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Заголовок как на скрине
            Text(
                text = "My Trips",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Табы Upcoming / History
            TripsTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Содержимое в зависимости от состояния
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "Failed to load trips.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                uiState.bookings.isEmpty() -> {
                    Text(
                        text = "You don’t have any trips yet.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    val listToShow = when (selectedTab) {
                        MyTripsTab.Upcoming -> upcomingBookings
                        MyTripsTab.History  -> historyBookings
                    }

                    if (listToShow.isEmpty()) {
                        Text(
                            text = when (selectedTab) {
                                MyTripsTab.Upcoming -> "No upcoming trips."
                                MyTripsTab.History  -> "No past trips yet."
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(listToShow) { booking ->
                                BookingTripCard(booking = booking)
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class MyTripsTab {
    Upcoming, History
}

/**
 * Контейнер с двумя табами.
 */
@Composable
private fun TripsTabs(
    selectedTab: MyTripsTab,
    onTabSelected: (MyTripsTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE4ECFF),
                shape = RoundedCornerShape(999.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabChip(
            text = "Upcoming",
            selected = selectedTab == MyTripsTab.Upcoming,
            onClick = { onTabSelected(MyTripsTab.Upcoming) },
            modifier = Modifier.weight(1f)
        )
        TabChip(
            text = "History",
            selected = selectedTab == MyTripsTab.History,
            onClick = { onTabSelected(MyTripsTab.History) },
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Одна таб-кнопка.
 */
@Composable
private fun TabChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container = if (selected) Color.White else Color.Transparent
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        colors = CardDefaults.cardColors(
            containerColor = container
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

/**
 * Карточка одной поездки, максимально близкая к макету.
 */
@Composable
private fun BookingTripCard(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Статус вверху
            BookingStatusChip(status = booking.status)

            Spacer(modifier = Modifier.height(10.dp))

            // Маршрут (две точки)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RouteRow(
                    dotColor = StatusConfirmed, // синий кружок
                    text = booking.routeName
                )
                RouteRow(
                    dotColor = Color.Black,
                    text = booking.dropoffLocation
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Нижняя строка: дата и время
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = booking.date,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = booking.time,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Строка с маленькой точкой и текстом.
 */
@Composable
private fun RouteRow(
    dotColor: Color,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(dotColor, shape = RoundedCornerShape(50))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Чип статуса: CONFIRMED / PENDING / COMPLETED / CANCELLED.
 */
@Composable
private fun BookingStatusChip(status: String) {
    val (label, bgColor, textColor) = when (status.lowercase()) {
        "confirmed" -> Triple("CONFIRMED", StatusConfirmed.copy(alpha = 0.15f), StatusConfirmed)
        "pending"   -> Triple("PENDING", StatusPending.copy(alpha = 0.18f), StatusPending)
        "cancelled" -> Triple("CANCELLED", StatusCancelled.copy(alpha = 0.18f), StatusCancelled)
        "completed" -> Triple("COMPLETED", StatusConfirmed.copy(alpha = 0.15f), StatusConfirmed)
        else        -> Triple(
            status.uppercase(),
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurface
        )
    }

    Box(
        modifier = Modifier
            .background(
                color = bgColor,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
