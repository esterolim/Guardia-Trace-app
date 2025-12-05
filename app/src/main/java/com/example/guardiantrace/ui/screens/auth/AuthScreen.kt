package com.example.guardiantrace.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import com.example.guardiantrace.ui.components.SecureTextField


@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App icon
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Guardian Trace",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App name
            Text(
                text = "Guardian Trace",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Your digital safety companion",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // PIN input
            SecureTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= 6) {
                        pin = it
                        isError = false
                        errorMessage = null
                    }
                },
                label = "Enter PIN",
                isError = isError,
                errorMessage = errorMessage,
                onImeAction = {
                    if (pin.length >= 4) {
                        // Simulate PIN validation (replace with real logic)
                        if (pin == "1234") {
                            onAuthSuccess()
                        } else {
                            isError = true
                            errorMessage = "Invalid PIN"
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Unlock button
            Button(
                onClick = {
                    if (pin.length >= 4) {
                        // Simulate PIN validation
                        if (pin == "1234") {
                            onAuthSuccess()
                        } else {
                            isError = true
                            errorMessage = "Invalid PIN"
                        }
                    } else {
                        isError = true
                        errorMessage = "PIN must be at least 4 digits"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = pin.isNotEmpty()
            ) {
                Text("Unlock")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Biometric button
            OutlinedButton(
                onClick = {
                    // Simulate biometric auth
                    onAuthSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Biometric",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Use Biometric")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Help text
            Text(
                text = "For demo: use PIN 1234",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}