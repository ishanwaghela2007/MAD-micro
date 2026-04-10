package com.example.myapplication.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.VitalityGreen
import com.example.myapplication.ui.theme.VitalityGreenDark
import com.example.myapplication.ui.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ─── Helpers (same logic as WorkoutScreen) ────────────────────────────────────
private fun isMealHeader(line: String): Boolean =
    line.startsWith("##") ||
    line.startsWith("**") && line.endsWith("**") && line.length > 4 ||
    line.startsWith("Breakfast") || line.startsWith("Lunch") ||
    line.startsWith("Dinner") || line.startsWith("Snack") ||
    line.startsWith("Morning") || line.startsWith("Evening")

private fun cleanMealHeader(line: String): String =
    line.removePrefix("##").removePrefix("**").removeSuffix("**").trim()

@Composable
fun MealPlanScreen(
    onMealClick: (MealPlanItem) -> Unit,
    onBack: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.fetchMealPlan() }

    val mealPlan = viewModel.mealPlan
    val date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd"))

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Daily Meal Plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    ) { innerPadding ->

        if (viewModel.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator(color = VitalityGreen, strokeWidth = 3.dp)
                    Text("Generating your meal plan…", color = MaterialTheme.colorScheme.tertiary)
                }
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            // ── Macro summary ─────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                MacroSummaryGrid(
                    calories = mealPlan?.nutrients?.calories?.toInt() ?: 0,
                    protein  = mealPlan?.nutrients?.protein?.toInt()  ?: 0,
                    carbs    = mealPlan?.nutrients?.carbs?.toInt()    ?: 0
                )
                Spacer(Modifier.height(28.dp))
            }

            // ── Section label ─────────────────────────────────────────────────
            item {
                Text(
                    text = "Your Meal Plan",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = VitalityGreen
                )
                Spacer(Modifier.height(12.dp))
            }

            // ── Parsed plan lines ─────────────────────────────────────────────
            val planText = mealPlan?.dailyPlan ?: ""
            if (planText.isNotBlank()) {
                val lines = planText
                    .split("\n")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                items(lines) { line ->
                    if (isMealHeader(line)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(VitalityGreen)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = cleanMealHeader(line),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = VitalityGreen
                            )
                        }
                    } else {
                        val annotated = buildAnnotatedString {
                            var remaining = line.removePrefix("-").removePrefix("•").trim()
                            while (remaining.contains("**")) {
                                val start = remaining.indexOf("**")
                                append(remaining.substring(0, start))
                                remaining = remaining.substring(start + 2)
                                val end = remaining.indexOf("**").takeIf { it >= 0 } ?: remaining.length
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)) {
                                    append(remaining.substring(0, end))
                                }
                                remaining = if (end + 2 <= remaining.length) remaining.substring(end + 2) else ""
                            }
                            append(remaining)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 3.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("•  ", color = VitalityGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    text = annotated,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = "No meal plan yet. Go to the Dashboard and tap\n\"Plan My Meal & Workout\".",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // ── Buttons ───────────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(28.dp))

                // Regenerate
                Button(
                    onClick = { viewModel.fetchMealPlan() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
                    enabled = !viewModel.isLoading,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("↺  Regenerate Plan", color = VitalityGreen, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(14.dp))

                // Save to Notes
                if (mealPlan != null) {
                    val noteContent = buildMealNote(
                        plan       = mealPlan.dailyPlan,
                        calories   = mealPlan.nutrients.calories.toInt(),
                        protein    = mealPlan.nutrients.protein.toInt(),
                        carbs      = mealPlan.nutrients.carbs.toInt(),
                        fats       = mealPlan.nutrients.fats.toInt()
                    )

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
                            .padding(horizontal = 24.dp)
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
            }
        }
    }
}

// ─── Macro grid ───────────────────────────────────────────────────────────────
@Composable
fun MacroSummaryGrid(calories: Int, protein: Int, carbs: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MacroItem("Calories", String.format("%,d", calories), "Kcal", Modifier.weight(1f))
        MacroItem("Protein",  "${protein}g",                  "Goal", Modifier.weight(1f))
        MacroItem("Carbs",    "${carbs}g",                    "Goal", Modifier.weight(1f))
    }
}

@Composable
fun MacroItem(label: String, value: String, unit: String, modifier: Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = VitalityGreen)
            Text(text = unit,  style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
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
                    Text(
                        text = "${slot.calories} kcal • ${slot.protein}g Protein",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { onClick(slot) }, contentPadding = PaddingValues(0.dp)) {
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
    MealPlanItem("Breakfast", "08:30 AM", "Avocado Toast & Egg",       420, 18, "https://images.unsplash.com/photo-1525351484163-7529414344d8?q=80&w=400"),
    MealPlanItem("Lunch",     "01:30 PM", "Grilled Chicken Quinoa",    580, 42, "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=400"),
    MealPlanItem("Dinner",    "08:00 PM", "Pan-Seared Salmon",         620, 45, "https://images.unsplash.com/photo-1467003909585-2f8a72700288?q=80&w=400"),
    MealPlanItem("Snacks",    "11:00 AM", "Greek Yogurt with Berries", 230, 20, "https://images.unsplash.com/photo-1488477181946-6428a0291777?q=80&w=400")
)

// ─── Note builder ─────────────────────────────────────────────────────────────
private fun buildMealNote(plan: String, calories: Int, protein: Int, carbs: Int, fats: Int): String = buildString {
    appendLine("🥗 My AI Meal Plan – Vitality Flow")
    appendLine("══════════════════════════════")
    appendLine()
    appendLine("── NUTRITION SUMMARY ──")
    appendLine("Calories : $calories kcal")
    appendLine("Protein  : ${protein}g")
    appendLine("Carbs    : ${carbs}g")
    appendLine("Fats     : ${fats}g")
    appendLine()
    if (plan.isNotBlank()) {
        appendLine("── MEAL PLAN ──")
        appendLine(plan.trim())
        appendLine()
    }
    appendLine("Generated by Vitality Flow")
}
