package com.example.guardiantrace.ui.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Home : Screen("home")
    data object IncidentList : Screen("incident_list")
    data object IncidentDetail : Screen("incident_detail/{incidentId}") {
        fun createRoute(incidentId: Long) = "incident_detail/$incidentId"
    }
    data object CreateIncident : Screen("create_incident")
    data object Settings : Screen("settings")
    data object EmergencyContacts : Screen("emergency_contacts")
    data object Export : Screen("export")
}