package com.example.guardiantrace.ui.screens.incident

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.guardiantrace.ui.components.GuardianTopBar
import com.example.guardiantrace.ui.components.IncidentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit
) {
    // Mock data for prototype
    var incidents by remember {
        mutableStateOf(
            listOf(
                MockIncident(
                    id = 1L,
                    title = "Harassment at workplace",
                    description = "Verbal harassment by colleague during meeting",
                    timestamp = "2 hours ago",
                    hasLocation = true,
                    attachmentCount = 2
                ),
                MockIncident(
                    id = 2L,
                    title = "Suspicious following",
                    description = "Noticed same person following me for 3 blocks",
                    timestamp = "Yesterday",
                    hasLocation = true,
                    attachmentCount = 1
                ),
                MockIncident(
                    id = 3L,
                    title = "Threatening messages",
                    description = "Received threatening text messages from unknown number",
                    timestamp = "3 days ago",
                    hasLocation = false,
                    attachmentCount = 5
                )
            )
        )
    }

    Scaffold(
        topBar = {
            GuardianTopBar(
                title = "My Incidents",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create incident"
                )
            }
        }
    ) { paddingValues ->
        if (incidents.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "No incidents",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "No incidents recorded",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tap + to create your first incident record",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // List of incidents
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(incidents) { incident ->
                    IncidentCard(
                        title = incident.title,
                        description = incident.description,
                        timestamp = incident.timestamp,
                        hasLocation = incident.hasLocation,
                        attachmentCount = incident.attachmentCount,
                        onClick = { onNavigateToDetail(incident.id) }
                    )
                }
            }
        }
    }
}

/**
 * Mock incident data for prototype
 */
private data class MockIncident(
    val id: Long,
    val title: String,
    val description: String,
    val timestamp: String,
    val hasLocation: Boolean,
    val attachmentCount: Int
)