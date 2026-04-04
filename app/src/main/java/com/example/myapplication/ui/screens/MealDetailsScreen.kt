package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
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
fun MealDetailsScreen(onBack: () -> Unit) {
    val sampleMeal = MealPlanItem(
        slotName = "Dinner",
        time = "08:00 PM",
        mealName = "Seared Atlantic Salmon",
        calories = 542,
        protein = 42,
        imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=800",
        ingredients = listOf("Fresh Salmon Fillet", "Extra Virgin Olive Oil", "Fresh Lemon", "Asparagus Spears", "Sea Salt & Cracked Pepper", "Fresh Dill"),
        steps = listOf(
            "Preheat your oven to 400\u00B0F (200\u00B0C). Line a large baking sheet with parchment paper for easy clean-up.",
            "Place the salmon fillets and asparagus on the baking sheet. Drizzle with olive oil and season with salt, pepper, and lemon juice.",
            "Bake for 12-15 minutes until the salmon is cooked through and the asparagus is tender.",
            "Garnish with fresh dill and serve immediately."
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                MealHeader(sampleMeal, onBack)
                Spacer(modifier = Modifier.height(24.dp))
                MealDescription(sampleMeal)
                Spacer(modifier = Modifier.height(24.dp))
                StatsGrid(sampleMeal)
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Ingredients",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            itemsIndexed(sampleMeal.ingredients) { index, ingredient ->
                IngredientItem(ingredient, index)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Preparation",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            itemsIndexed(sampleMeal.steps) { index, step ->
                StepItem(step, index)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = { onBack() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalityGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Add to Meal Plan", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun MealHeader(meal: MealPlanItem, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
        AsyncImage(
            model = meal.imageUrl,
            contentDescription = meal.mealName,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack, modifier = Modifier.clip(CircleShape).background(Color.Black.copy(alpha = 0.5f))) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            IconButton(onClick = {}, modifier = Modifier.clip(CircleShape).background(Color.Black.copy(alpha = 0.5f))) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
        }
    }
}

@Composable
fun MealDescription(meal: MealPlanItem) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Surface(color = VitalityGreen.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)) {
            Text(
                text = "Premium Recipe",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = VitalityGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = meal.mealName, style = MaterialTheme.typography.headlineLarge, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "32 min \u2022 High Protein", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
fun StatsGrid(meal: MealPlanItem) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StatPill("CALORIES", "${meal.calories}")
        StatPill("PROTEIN", "${meal.protein}g")
        StatPill("CARBS", "12g")
        StatPill("FAT", "28g")
    }
}

@Composable
fun StatPill(label: String, value: String) {
    Surface(
        modifier = Modifier.width(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.tertiary)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }
    }
}

@Composable
fun IngredientItem(ingredient: String, index: Int) {
    Surface(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(VitalityGreen))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = ingredient, fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun StepItem(step: String, index: Int) {
    Row(modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth()) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(VitalityGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "${index + 1}", fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = step, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
    }
}
