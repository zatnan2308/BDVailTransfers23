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
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import com.example.bdvailtransfers2.NavRoutes
    import com.example.bdvailtransfers2.ui.components.BDButton
    import com.example.bdvailtransfers2.ui.components.BDCard
    import com.example.bdvailtransfers2.ui.viewmodel.RoutesUiState
    import com.example.bdvailtransfers2.ui.viewmodel.RoutesViewModel

    /**
     * Экран со списком реальных маршрутов из API.
     */
    @Composable
    fun RoutesScreen(
        navController: NavController,
        routesViewModel: RoutesViewModel = viewModel()
    ) {
        val uiState by routesViewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "All routes",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is RoutesUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Loading routes…")
                    }
                }

                is RoutesUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        BDButton(
                            text = "Retry",
                            onClick = { routesViewModel.loadRoutes(forceRefresh = true) }
                        )
                    }
                }

                is RoutesUiState.Success -> {
                    val routes = state.routes

                    if (routes.isEmpty()) {
                        Text(
                            text = "No routes available yet.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(routes) { route ->
                                val title = if (route.from.isNotBlank() && route.to.isNotBlank()) {
                                    "${route.from} → ${route.to}"
                                } else {
                                    route.name
                                }

                                val subtitle = buildString {
                                    append(route.name)
                                    if (route.basePrice > 0.0) {
                                        append(" · from ")
                                        append(route.basePrice.toInt())
                                        append(" ")
                                        append(route.currency)
                                    }
                                    if (route.durationMinutes > 0) {
                                        append(" · ")
                                        append(route.durationMinutes / 60)
                                        append("h")
                                        val minutes = route.durationMinutes % 60
                                        if (minutes > 0) {
                                            append(" ")
                                            append(minutes)
                                            append("m")
                                        }
                                    }
                                }

                                BDCard(
                                    title = title,
                                    subtitle = subtitle,
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        navController.navigate(NavRoutes.BOOKING_FORM + "/${route.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
