package com.example.bdvailtransfers2.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bdvailtransfers2.NavRoutes
import com.example.bdvailtransfers2.ui.components.BDButton
import com.example.bdvailtransfers2.ui.components.BDTextField
import com.example.bdvailtransfers2.ui.components.StepIndicator
import com.example.bdvailtransfers2.ui.theme.StatusConfirmed
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Экран New Booking — трёхшаговый мастер в стиле макета:
 *
 * Шаг 1: Pick-up / Drop-off Location
 * Шаг 2: Date / Time
 * Шаг 3: Name / Phone / Passengers + Confirm Booking
 *
 * Пока всё хранится в локальном состоянии.
 * При подтверждении просто переходим на экран успешного бронирования.
 */
@Composable
fun BookingFormScreen(
    navController: NavController,
    routeId: String?
) {
    val context = LocalContext.current

    // Текущий шаг (1..3)
    var currentStep by remember { mutableStateOf(1) }

    // Поля формы
    var pickupLocation by remember { mutableStateOf("Select Location") }
    var dropoffLocation by remember { mutableStateOf("Select Location") }

    var date by remember { mutableStateOf("Select Date") }
    var time by remember { mutableStateOf("Select Time") }

    var name by remember { mutableStateOf("Guest") }
    var phone by remember { mutableStateOf("+1...") }

    var passengers by remember { mutableStateOf(1) }
    val maxPassengers = 2 // как на макете "Max available: 2"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {

        // Заголовок + StepIndicator
        Text(
            text = "New Booking",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        StepIndicator(
            currentStep = currentStep,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (currentStep) {
            1 -> {
                Step1Locations(
                    pickupLocation = pickupLocation,
                    dropoffLocation = dropoffLocation,
                    onPickupClick = {
                        // Здесь позже можно открыть настоящий список маршрутов
                        // Пока просто меняем тестовое значение
                        pickupLocation = "Select Location"
                    },
                    onDropoffClick = {
                        dropoffLocation = "Select Location"
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                BDButton(
                    text = "Next: Date & Time",
                    onClick = { currentStep = 2 }
                )
            }

            2 -> {
                Step2DateTime(
                    date = date,
                    time = time,
                    onSelectDate = {
                        val calendar = Calendar.getInstance()
                        val dialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val cal = Calendar.getInstance()
                                cal.set(year, month, dayOfMonth)
                                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                                date = format.format(cal.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        dialog.show()
                    },
                    onSelectTime = {
                        val calendar = Calendar.getInstance()
                        val dialog = TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val format = SimpleDateFormat("HH:mm", Locale.US)
                                val cal = Calendar.getInstance()
                                cal.set(Calendar.HOUR_OF_DAY, hour)
                                cal.set(Calendar.MINUTE, minute)
                                time = format.format(cal.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        )
                        dialog.show()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { currentStep = 1 },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    BDButton(
                        text = "Next",
                        onClick = { currentStep = 3 },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            3 -> {
                Step3PassengerDetails(
                    name = name,
                    phone = phone,
                    passengers = passengers,
                    maxPassengers = maxPassengers,
                    onNameChange = { name = it },
                    onPhoneChange = { phone = it },
                    onPassengersChange = { newCount ->
                        passengers = newCount.coerceIn(1, maxPassengers)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                BDButton(
                    text = "Confirm Booking",
                    onClick = {
                        // На отдельном этапе можно будет вызвать BookingViewModel.submitBooking(...)
                        navController.navigate(NavRoutes.BOOKING_SUCCESS)
                    },
                    backgroundColor = StatusConfirmed,
                    contentColor = Color.White
                )
            }
        }
    }
}

/**
 * Шаг 1: выбор точек отправления и назначения.
 */
@Composable
private fun Step1Locations(
    pickupLocation: String,
    dropoffLocation: String,
    onPickupClick: () -> Unit,
    onDropoffClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationCard(
            label = "Pick-up Location",
            value = pickupLocation,
            onClick = onPickupClick
        )
        LocationCard(
            label = "Drop-off Location",
            value = dropoffLocation,
            onClick = onDropoffClick
        )
    }
}

/**
 * Карточка локации, стилизованная как на скрине.
 */
@Composable
private fun LocationCard(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📍",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
    }
}

/**
 * Шаг 2: выбор даты и времени.
 */
@Composable
private fun Step2DateTime(
    date: String,
    time: String,
    onSelectDate: () -> Unit,
    onSelectTime: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DateTimeCard(
            label = "Date",
            value = date,
            onClick = onSelectDate
        )
        DateTimeCard(
            label = "Time",
            value = time,
            onClick = onSelectTime
        )
    }
}

/**
 * Карточка выбора даты/времени.
 */
@Composable
private fun DateTimeCard(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
                Text(
                    text = "▾",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

/**
 * Шаг 3: имя, телефон, количество пассажиров.
 */
@Composable
private fun Step3PassengerDetails(
    name: String,
    phone: String,
    passengers: Int,
    maxPassengers: Int,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPassengersChange: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BDTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Your Name"
        )

        BDTextField(
            value = phone,
            onValueChange = onPhoneChange,
            label = "Your Phone"
        )

        Column {
            Text(
                text = "Passengers",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Passengers",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircleIconButton(
                                text = "-",
                                enabled = passengers > 1,
                                onClick = { onPassengersChange(passengers - 1) }
                            )
                            Text(
                                text = passengers.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            CircleIconButton(
                                text = "+",
                                enabled = passengers < maxPassengers,
                                onClick = { onPassengersChange(passengers + 1) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Max available: $maxPassengers",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

/**
 * Круглая маленькая кнопка +- для пассажиров.
 */
@Composable
private fun CircleIconButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(
                color = if (enabled)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                shape = CircleShape
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}
