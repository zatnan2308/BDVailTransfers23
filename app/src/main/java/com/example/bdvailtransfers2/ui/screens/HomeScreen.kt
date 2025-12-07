package com.example.bdvailtransfers2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bdvailtransfers2.NavRoutes
import com.example.bdvailtransfers2.ui.components.BDButton
import com.example.bdvailtransfers2.ui.components.BDCard

data class PopularRoute(
    val id: String,
    val title: String,
    val subtitle: String
)

val popularRoutes = listOf(
    PopularRoute(
        id = "denver_to_vail",
        title = "Denver → Vail",
        subtitle = "Private AWD transfer · 2–2.5 hours"
    ),
    PopularRoute(
        id = "dia_to_beaver_creek",
        title = "DEN Airport → Beaver Creek",
        subtitle = "Meet & Greet at DIA · Child seats available"
    ),
    PopularRoute(
        id = "vail_to_beaver_creek",
        title = "Vail → Beaver Creek",
        subtitle = "Resort-to-resort shuttle · Flexible timing"
    )
)

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "BDVail Transfers",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Private AWD transfers between Denver, Vail, Beaver Creek and more.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        BDButton(
            text = "Request transfer",
            onClick = { navController.navigate(NavRoutes.BOOKING_FORM) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BDButton(
            text = "See all routes",
            onClick = { navController.navigate(NavRoutes.ROUTES) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Popular routes",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(popularRoutes) { route ->
                BDCard(
                    title = route.title,
                    subtitle = route.subtitle,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(NavRoutes.BOOKING_FORM + "/${route.id}")
                    }
                )
            }
        }
    }
}
