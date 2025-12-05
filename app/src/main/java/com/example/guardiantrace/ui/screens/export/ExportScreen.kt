package com.example.guardiantrace.ui.screens.export

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guardiantrace.ui.components.GuardianTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    onNavigateBack: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf(ExportFormat.PDF) }
    var includeAttachments by remember { mutableStateOf(true) }
    var includeLocation by remember { mutableStateOf(true) }
    var includeMetadata by remember { mutableStateOf(true) }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GuardianTopBar(
                title = "Export Data",
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Info card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Text(
                        text = "Export your incident records for legal purposes or personal backup",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Export format section
            Text(
                text = "Export Format",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ExportFormatOption(
                        format = ExportFormat.PDF,
                        selected = selectedFormat == ExportFormat.PDF,
                        onSelect = { selectedFormat = ExportFormat.PDF }
                    )

                    ExportFormatOption(
                        format = ExportFormat.JSON,
                        selected = selectedFormat == ExportFormat.JSON,
                        onSelect = { selectedFormat = ExportFormat.JSON }
                    )

                    ExportFormatOption(
                        format = ExportFormat.CSV,
                        selected = selectedFormat == ExportFormat.CSV,
                        onSelect = { selectedFormat = ExportFormat.CSV }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Export options section
            Text(
                text = "Include in Export",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ExportCheckboxOption(
                        title = "Attachments",
                        description = "Include photos, videos, and audio files",
                        checked = includeAttachments,
                        onCheckedChange = { includeAttachments = it }
                    )

                    ExportCheckboxOption(
                        title = "Location Data",
                        description = "Include GPS coordinates and addresses",
                        checked = includeLocation,
                        onCheckedChange = { includeLocation = it }
                    )

                    ExportCheckboxOption(
                        title = "Metadata",
                        description = "Include timestamps and device information",
                        checked = includeMetadata,
                        onCheckedChange = { includeMetadata = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Export summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Export Summary",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SummaryRow(label = "Total Incidents", value = "3")
                    SummaryRow(label = "Total Attachments", value = "8")
                    SummaryRow(label = "Format", value = selectedFormat.displayName)
                    SummaryRow(
                        label = "Estimated Size",
                        value = if (includeAttachments) "~15 MB" else "~500 KB"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Success message
            if (exportSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Text(
                            text = "Export completed successfully! File saved to Downloads.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Export button
            Button(
                onClick = {
                    isExporting = true
                    // Simulate export
                    exportSuccess = true
                    isExporting = false
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isExporting
            ) {
                Text(if (isExporting) "Exporting..." else "Export Data")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Share button
            if (exportSuccess) {
                OutlinedButton(
                    onClick = { /* TODO: Share */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share Exported File")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Export format option with radio button
 */
@Composable
private fun ExportFormatOption(
    format: ExportFormat,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )

        Spacer(modifier = Modifier.size(12.dp))

        Icon(
            imageVector = format.icon,
            contentDescription = format.displayName,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(
                text = format.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = format.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Export checkbox option
 */
@Composable
private fun ExportCheckboxOption(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private enum class ExportFormat(
    val displayName: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    PDF(
        displayName = "PDF Document",
        description = "Best for legal purposes and printing",
        icon = Icons.Default.PictureAsPdf
    ),
    JSON(
        displayName = "JSON File",
        description = "Machine-readable format with all data",
        icon = Icons.Default.Description
    ),
    CSV(
        displayName = "CSV Spreadsheet",
        description = "Compatible with Excel and Google Sheets",
        icon = Icons.Default.Description
    )
}