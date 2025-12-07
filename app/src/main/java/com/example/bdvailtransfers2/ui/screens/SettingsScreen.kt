package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var darkTheme by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(
                text = "Dark theme",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = darkTheme,
                onCheckedChange = { darkTheme = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Text(
                text = "Notifications",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "App version 0.1 (demo)",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
