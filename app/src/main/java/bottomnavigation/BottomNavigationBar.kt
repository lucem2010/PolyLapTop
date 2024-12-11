package bottomnavigation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 0.dp)
            .padding(bottom = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFFC720D), RoundedCornerShape(16.dp)) // Border with rounded corners
    ) {
        BottomNavigation(
            modifier = Modifier
                .navigationBarsPadding() // Adds padding for system navigation bars
                .background(Color(0xFFFC720D)), // Set background color
            backgroundColor = Color(0xFFFC720D),
            contentColor = Color.White // Set default content color (icon and text when selected)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            listOf(
                BottomNavItem.Home,
                BottomNavItem.Cart,
                BottomNavItem.Order,
                BottomNavItem.Setting
            ).forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = null,
                            tint = if (currentRoute == item.route) Color.White else Color.LightGray
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            fontSize = 12.sp,
                            color = if (currentRoute == item.route) Color.White else Color.LightGray
                        )
                    },
                    alwaysShowLabel = false // Only show label when item is selected
                )
            }
        }
    }
}

