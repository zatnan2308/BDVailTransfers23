package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bdvailtransfers2.ui.components.BDButton
import com.example.bdvailtransfers2.ui.components.BDTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SupportScreen() {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Support",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        BDTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name"
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = contact,
            onValueChange = { contact = it },
            label = "Phone / Email"
        )

        Spacer(modifier = Modifier.height(8.dp))

        BDTextField(
            value = message,
            onValueChange = { message = it },
            label = "Message",
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        BDButton(
            text = "Send",
            onClick = { /* пока заглушка */ }
        )
    }
}
