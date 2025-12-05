package com.example.guardiantrace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.guardiantrace.ui.screens.auth.AuthScreen
import com.example.guardiantrace.ui.screens.contacts.EmergencyContactsScreen
import com.example.guardiantrace.ui.screens.export.ExportScreen
import com.example.guardiantrace.ui.screens.home.HomeScreen
import com.example.guardiantrace.ui.screens.incident.CreateIncidentScreen
import com.example.guardiantrace.ui.screens.incident.IncidentDetailScreen
import com.example.guardiantrace.ui.screens.incident.IncidentListScreen
import com.example.guardiantrace.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Auth.route
) {
    // Simulate authentication state (will be replaced with real ViewModel)
    var isAuthenticated by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) Screen.Home.route else Screen.Auth.route
    ) {
        // Auth Screen
        composable(Screen.Auth.route) {
            AuthScreen(
                onAuthSuccess = {
                    isAuthenticated = true
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToIncidents = {
                    navController.navigate(Screen.IncidentList.route)
                },
                onNavigateToCreateIncident = {
                    navController.navigate(Screen.CreateIncident.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToContacts = {
                    navController.navigate(Screen.EmergencyContacts.route)
                },
                onNavigateToExport = {
                    navController.navigate(Screen.Export.route)
                }
            )
        }

        // Incident List Screen
        composable(Screen.IncidentList.route) {
            IncidentListScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToDetail = { incidentId ->
                    navController.navigate(Screen.IncidentDetail.createRoute(incidentId))
                },
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateIncident.route)
                }
            )
        }

        // Incident Detail Screen
        composable(
            route = Screen.IncidentDetail.route,
            arguments = listOf(
                navArgument("incidentId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val incidentId = backStackEntry.arguments?.getLong("incidentId") ?: 0L
            IncidentDetailScreen(
                incidentId = incidentId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Create Incident Screen
        composable(Screen.CreateIncident.route) {
            CreateIncidentScreen(
                onNavigateBack = { navController.navigateUp() },
                onIncidentCreated = {
                    navController.navigateUp()
                }
            )
        }

        // Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Emergency Contacts Screen
        composable(Screen.EmergencyContacts.route) {
            EmergencyContactsScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Export Screen
        composable(Screen.Export.route) {
            ExportScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}