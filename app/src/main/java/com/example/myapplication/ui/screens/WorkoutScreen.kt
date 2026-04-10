package com.example.myapplication.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.network.WorkoutExercise
import com.example.myapplication.ui.theme.VitalityGreen
import com.example.myapplication.ui.theme.VitalityGreenDark
import com.example.myapplication.ui.viewmodel.MainViewModel

// ─── Intensity colour helper ───────────────────────────────────────────────────
private fun intensityColor(intensity: String): Color = when {
    intensity.contains("high", ignoreCase = true)   -> Color(0xFFFF5252)
    intensity.contains("medium", ignoreCase = true) -> Color(0xFFFFB300)
    intensity.contains("low", ignoreCase = true)    -> Color(0xFF69F0AE)
    else                                            -> Color(0xFF90CAF9)
}

// ─── Detect markdown-style section headers ────────────────────────────────────
private fun isHeader(line: String): Boolean =
    line.startsWith("##") ||
    line.startsWith("**Day") ||
    line.startsWith("Day ") ||
    (line.startsWith("**") && line.endsWith("**") && line.length > 4)

private fun cleanHeader(line: String): String =
    line.removePrefix("##").removePrefix("**").removeSuffix("**").trim()

// ─── Main screen ──────────────────────────────────────────────────────────────
@Composable
fun WorkoutScreen(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.fetchWorkout() }

    val workout   = viewModel.workout
    val isLoading = viewModel.isLoading
    val error     = viewModel.error

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        CircularProgressIndicator(color = VitalityGreen, strokeWidth = 3.dp)
                        Text("Generating your workout…", color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }

            error != null && workout == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Something went wrong:\n$error", color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {

                    // ── Header ────────────────────────────────────────────────
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(VitalityGreen.copy(alpha = 0.15f), Color.Transparent)
                                    )
                                )
                                .padding(horizontal = 24.dp, vertical = 28.dp)
                        ) {
                            Column {
                                Text(
                                    text = "AI Workout Plan",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = "Personalised to your fitness profile",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = VitalityGreen
                                )
                            }
                        }
                    }

                    // ── Workout plan text ─────────────────────────────────────
                    if (!workout?.workoutPlan.isNullOrBlank()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Your Plan",
                                modifier = Modifier.padding(horizontal = 24.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VitalityGreen
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        val lines = (workout?.workoutPlan ?: "")
                            .split("\n")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        items(lines) { line ->
                            if (isHeader(line)) {
                                // Section header row
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
                                        text = cleanHeader(line),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = VitalityGreen
                                    )
                                }
                            } else {
                                // Body line — render inline **bold** markers
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
                                        Text(
                                            text = "•  ",
                                            color = VitalityGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
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
                    }

                    // ── Exercises section ─────────────────────────────────────
                    if (!workout?.exercises.isNullOrEmpty()) {
                        item {
                            Spacer(Modifier.height(24.dp))
                            Text(
                                text = "Exercises",
                                modifier = Modifier.padding(horizontal = 24.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = VitalityGreen
                            )
                            Spacer(Modifier.height(12.dp))
                        }

                        items(workout?.exercises ?: emptyList()) { exercise ->
                            AnimatedVisibility(visible = true, enter = fadeIn() + expandVertically()) {
                                ExerciseCard(exercise)
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }

                    // ── Empty state ───────────────────────────────────────────
                    if (workout == null) {
                        item {
                            Box(
                                Modifier.fillMaxWidth().padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(48.dp))
                                    Text("No workout plan yet.\nGo to the Dashboard and tap\n\"Plan My Meal & Workout\".", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                        }
                    }

                    // ── Save to Notes button ──────────────────────────────────
                    if (workout != null) {
                        item {
                            Spacer(Modifier.height(24.dp))

                            val noteContent = buildNoteContent(workout.workoutPlan, workout.exercises)

                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "My AI Workout Plan – Vitality Flow")
                                        putExtra(Intent.EXTRA_TEXT, noteContent)
                                    }
                                    context.startActivity(
                                        Intent.createChooser(intent, "Save to Notes / Share")
                                    )
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
                                        )
                                        .border(0.dp, Color.Transparent, RoundedCornerShape(16.dp)),
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
    }
}

// ─── Exercise card with expandable details ────────────────────────────────────
@Composable
fun ExerciseCard(exercise: WorkoutExercise) {
    var expanded by remember { mutableStateOf(false) }
    val badgeColor = intensityColor(exercise.intensity)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Icon box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VitalityGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = VitalityGreen, modifier = Modifier.size(24.dp))
                }

                Spacer(Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        // Sets × Reps chip
                        StatChip("${exercise.sets} sets × ${exercise.reps}", VitalityGreen.copy(alpha = 0.15f), VitalityGreen)
                        // Intensity badge
                        StatChip(exercise.intensity, badgeColor.copy(alpha = 0.15f), badgeColor)
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            // Expanded detail panel
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    Spacer(Modifier.height(12.dp))

                    DetailRow(label = "Exercise", value = exercise.name)
                    DetailRow(label = "Sets", value = exercise.sets.toString())
                    DetailRow(label = "Reps", value = exercise.reps)
                    DetailRow(label = "Intensity", value = exercise.intensity, valueColor = badgeColor)
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textColor)
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onBackground) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

// ─── Build the plain-text note ─────────────────────────────────────────────
private fun buildNoteContent(plan: String, exercises: List<WorkoutExercise>): String = buildString {
    appendLine("🏋️ My AI Workout Plan – Vitality Flow")
    appendLine("══════════════════════════════")
    appendLine()
    if (plan.isNotBlank()) {
        appendLine("── WORKOUT PLAN ──")
        appendLine(plan.trim())
        appendLine()
    }
    if (exercises.isNotEmpty()) {
        appendLine("── EXERCISES ──")
        exercises.forEachIndexed { i, ex ->
            appendLine("${i + 1}. ${ex.name}")
            appendLine("   Sets: ${ex.sets}   Reps: ${ex.reps}   Intensity: ${ex.intensity}")
        }
    }
    appendLine()
    appendLine("Generated by Vitality Flow")
}
