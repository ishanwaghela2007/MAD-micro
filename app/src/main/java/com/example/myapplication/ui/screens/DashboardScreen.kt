package com.example.myapplication.ui.screens

import android.content.Intent
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
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.VitalityGreen
import com.example.myapplication.ui.theme.VitalityGreenDark
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.viewmodel.MainViewModel

@Composable
fun DashboardScreen(
    onMealPlanClick: () -> Unit = {},
    onWorkoutClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
        viewModel.fetchMealPlan()
    }

    val profile = viewModel.profile
    val mealPlan = viewModel.mealPlan

    Scaffold(
        bottomBar = { VitalityBottomBar(onHome = {}, onWorkout = onWorkoutClick, onProfile = onProfileClick) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
        ) {
            item {
                HeaderSection(name = profile?.name ?: "User")
                Spacer(modifier = Modifier.height(32.dp))
                
                // New Button for Meal/Training Struct
                Button(
                    onClick = onMealPlanClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalityGreen)
                ) {
                    Text("Plan Your Meal & Workout", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Box(modifier = Modifier.clickable { onMealPlanClick() }) {
                    ProteinProgressCard(
                        current = mealPlan?.nutrients?.protein?.toInt() ?: 0,
                        goal = 120 // Example goal
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                StatsGrid(
                    calories = mealPlan?.nutrients?.calories?.toInt() ?: 0,
                    protein = mealPlan?.nutrients?.protein?.toInt() ?: 0
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "AI Suggested Meal Plan",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = mealPlan?.dailyPlan ?: "No meal plan generated yet. Tap 'Plan' to start.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Save to Notes button — only shown when a plan exists
                if (mealPlan != null) {
                    val noteContent = buildString {
                        appendLine("🥗 My AI Meal Plan – Vitality Flow")
                        appendLine("══════════════════════════════")
                        appendLine()
                        appendLine("── NUTRITION SUMMARY ──")
                        appendLine("Calories : ${mealPlan.nutrients.calories.toInt()} kcal")
                        appendLine("Protein  : ${mealPlan.nutrients.protein.toInt()}g")
                        appendLine("Carbs    : ${mealPlan.nutrients.carbs.toInt()}g")
                        appendLine("Fats     : ${mealPlan.nutrients.fats.toInt()}g")
                        appendLine()
                        appendLine("── MEAL PLAN ──")
                        appendLine(mealPlan.dailyPlan.trim())
                        appendLine()
                        appendLine("Generated by Vitality Flow")
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "My AI Meal Plan – Vitality Flow")
                                putExtra(Intent.EXTRA_TEXT, noteContent)
                            }
                            context.startActivity(Intent.createChooser(intent, "Save to Notes / Share"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(listOf(VitalityGreen, VitalityGreenDark)),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null, tint = Color.Black)
                                Text("Save to Notes", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(name: String) {
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
                text = "Hello, $name!",
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
fun ProteinProgressCard(current: Int, goal: Int) {
    val progress = if (goal > 0) current.toFloat() / goal else 0f
    val percentage = (progress * 100).toInt()

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
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = Stroke(width = 15.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${current}g", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "$percentage% complete", style = MaterialTheme.typography.labelSmall, color = VitalityGreen)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                StatSub("Consumed", "${current}g", VitalityGreen)
                StatSub("Remaining", "${(goal - current).coerceAtLeast(0)}g", Color.White)
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
fun StatsGrid(calories: Int, protein: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        StatCard("Calories", String.format("%,d", calories), "kcal", Modifier.weight(1f))
        StatCard("Protein", "${protein}g", "Goal", Modifier.weight(1f))
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
fun VitalityBottomBar(onHome: () -> Unit, onWorkout: () -> Unit, onProfile: () -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
        NavigationBarItem(selected = true, onClick = onHome, icon = { Icon(Icons.Default.Home, "") }, label = { Text("Home") })
        NavigationBarItem(selected = false, onClick = onWorkout, icon = { Icon(Icons.Default.Build, "") }, label = { Text("Workout") })
        NavigationBarItem(selected = false, onClick = onProfile, icon = { Icon(Icons.Default.Person, "") }, label = { Text("Profile") })
    }
}

data class MealData(val name: String, val icon: String, val cals: Int, val protein: Int)
val sampleMeals = listOf(
    MealData("Greek Yogurt Bowl", "\ud83e\udd63", 320, 24),
    MealData("Grilled Chicken Salad", "\ud83e\udd57", 450, 38),
    MealData("Protein Shake", "\ud83e\udd64", 180, 20)
)
