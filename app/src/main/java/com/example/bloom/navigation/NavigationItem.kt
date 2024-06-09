package com.example.bloom.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MobileFriendly
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.MobileFriendly
import androidx.compose.material.icons.outlined.People
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val nav_routes = listOf(
    NavigationItem(
        title = "Journal",
        selectedIcon = Icons.AutoMirrored.Filled.StickyNote2,
        unselectedIcon = Icons.AutoMirrored.Outlined.StickyNote2,
        route = Routes.JournalListScreen.route
    ),
    NavigationItem(
        title = "ChatRooms",
        selectedIcon = Icons.Filled.People,
        unselectedIcon = Icons.Outlined.People,
        route = Routes.ChatRoomsListScreen.route
    ),
    NavigationItem(
        title = "Ember",
        selectedIcon = Icons.Filled.MobileFriendly,
        unselectedIcon = Icons.Outlined.MobileFriendly,
        route = Routes.ChatBotScreen.route
    ),
    NavigationItem(
        title = "Doctor",
        selectedIcon = Icons.Filled.HealthAndSafety,
        unselectedIcon = Icons.Outlined.HealthAndSafety,
        route = Routes.DoctorScreen.route
    ),
)