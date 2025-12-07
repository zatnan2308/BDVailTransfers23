package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bdvailtransfers2.NavRoutes
import com.example.bdvailtransfers2.ui.components.BDButton
import com.example.bdvailtransfers2.ui.components.BDTextField
import kotlinx.coroutines.launch

@Composable
fun BookingFormScreen(
    navController: NavController,
    routeId: String?
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var route by remember { mutableStateOf(routeId ?: "") }
    var dateTime by remember { mutableStateOf("") }
    var passengers by remember { mutableStateOf("") }
    var childSeats by remember { mutableStateOf("") }
    var luggage by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)

        Text(
            text = "Transfer request",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        BDTextField(
            value = route,
            onValueChange = { route = it },
            label = "Route (e.g. Denver → Vail)",
            modifier = Modifier.fillMaxSize()
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = dateTime,
            onValueChange = { dateTime = it },
            label = "Date & time",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = passengers,
            onValueChange = { passengers = it },
            label = "Passengers",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = childSeats,
            onValueChange = { childSeats = it },
            label = "Child seats",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = luggage,
            onValueChange = { luggage = it },
            label = "Luggage",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Phone / Email",
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = comment,
            onValueChange = { comment = it },
            label = "Comment",
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        BDButton(
            text = "Send request",
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Request sent (demo)")
                }
                navController.navigate(NavRoutes.BOOKING_SUCCESS)
            }
        )
    }
}
