package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.VitalityGreen

@Composable
fun MealPlanScreen(onMealClick: (MealPlanItem) -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Daily Meal Plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = "Tuesday, Oct 24", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
        ) {
            item {
                MacroSummaryGrid()
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            items(mealSlots) { slot ->
                MealSlotCard(slot, onMealClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                Button(
                    onClick = { /* Generate new */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalityGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Generate Next Plan", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun MacroSummaryGrid() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MacroItem("Calories", "1,850", "Kcal", Modifier.weight(1f))
        MacroItem("Protein", "145g", "Goal", Modifier.weight(1f))
        MacroItem("Carbs", "210g", "Goal", Modifier.weight(1f))
    }
}

@Composable
fun MacroItem(label: String, value: String, unit: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VitalityGreen)
            Text(text = unit, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        }
    }
}

@Composable
fun MealSlotCard(slot: MealPlanItem, onClick: (MealPlanItem) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = slot.slotName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = slot.time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = slot.imageUrl,
                    contentDescription = slot.mealName,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = slot.mealName, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text(text = "${slot.calories} kcal \u2022 ${slot.protein}g Protein", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { onClick(slot) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("View Steps", color = VitalityGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class MealPlanItem(
    val slotName: String,
    val time: String,
    val mealName: String,
    val calories: Int,
    val protein: Int,
    val imageUrl: String,
    val ingredients: List<String> = emptyList(),
    val steps: List<String> = emptyList()
)

val mealSlots = listOf(
    MealPlanItem("Breakfast", "08:30 AM", "Avocado Toast & Egg", 420, 18, "https://images.unsplash.com/photo-1525351484163-7529414344d8?q=80&w=400"),
    MealPlanItem("Lunch", "01:30 PM", "Grilled Chicken Quinoa", 580, 42, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=400"),
    MealPlanItem("Dinner", "08:00 PM", "Pan-Seared Salmon", 620, 45, "https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=400"),
    MealPlanItem("Snacks", "11:00 AM", "Greek Yogurt with Berries", 230, 20, "https://images.unsplash.com/photo-1488477181946-6428a0291777?q=80&w=400")
)
