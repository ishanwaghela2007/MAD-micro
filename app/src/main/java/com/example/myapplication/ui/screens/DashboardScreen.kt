package com.example.myapplication.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.VitalityGreen
import com.example.myapplication.ui.theme.VitalityGreenDark

@Composable
fun DashboardScreen(
    onMealPlanClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {}
) {
    Scaffold(
        bottomBar = { VitalityBottomBar(onHome = {}, onSearch = onWorkoutClick, onProfile = {}) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
        ) {
            item {
                HeaderSection()
                Spacer(modifier = Modifier.height(32.dp))
                Box(modifier = Modifier.clickable { onMealPlanClick() }) {
                    ProteinProgressCard()
                }
                Spacer(modifier = Modifier.height(24.dp))
                StatsGrid()
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Today's Meals",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(sampleMeals) { meal ->
                MealCard(meal)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Hello, Alex!",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
        }
    }
}

@Composable
fun ProteinProgressCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Protein Goal",
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(160.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.DarkGray.copy(alpha = 0.3f),
                        style = Stroke(width = 15.dp.toPx())
                    )
                    drawArc(
                        color = VitalityGreen,
                        startAngle = -90f,
                        sweepAngle = 270f, // 75%
                        useCenter = false,
                        style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "65g", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "75% complete", style = MaterialTheme.typography.labelSmall, color = VitalityGreen)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                StatSub("Consumed", "45g", VitalityGreen)
                StatSub("Remaining", "20g", Color.White)
            }
        }
    }
}

@Composable
fun StatSub(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun StatsGrid() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        StatCard("Calories", "1,450", "kcal", Modifier.weight(1f))
        StatCard("Hydration", "1.8L", "L", Modifier.weight(1f))
    }
}

@Composable
fun StatCard(label: String, value: String, unit: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VitalityGreen)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

@Composable
fun MealCard(meal: MealData) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(meal.icon, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = meal.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "${meal.cals} kcal \u2022 ${meal.protein}g protein", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
        }
    }
}

@Composable
fun VitalityBottomBar(onHome: () -> Unit, onSearch: () -> Unit, onProfile: () -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        NavigationBarItem(selected = true, onClick = onHome, icon = { Icon(Icons.Default.Home, "") }, label = { Text("Home") })
        NavigationBarItem(selected = false, onClick = onSearch, icon = { Icon(Icons.Default.Search, "") }, label = { Text("Search") })
        NavigationBarItem(selected = false, onClick = onProfile, icon = { Icon(Icons.Default.Person, "") }, label = { Text("Profile") })
    }
}

data class MealData(val name: String, val icon: String, val cals: Int, val protein: Int)
val sampleMeals = listOf(
    MealData("Greek Yogurt Bowl", "\ud83e\udd63", 320, 24),
    MealData("Grilled Chicken Salad", "\ud83e\udd57", 450, 38),
    MealData("Protein Shake", "\ud83e\udd64", 180, 20)
)
