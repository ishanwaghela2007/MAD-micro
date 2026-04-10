package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.VitalityGreen
import com.example.myapplication.ui.viewmodel.MainViewModel

@Composable
fun ProfileSetupScreen(
    onComplete: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    var age by remember { mutableStateOf("24") }
    var height by remember { mutableStateOf("178") }
    var weight by remember { mutableStateOf("72") }
    var goalWeight by remember { mutableStateOf("68") }
    var gender by remember { mutableStateOf("Male") }
    var goalSelected by remember { mutableStateOf("Weight Loss") }
    var activitySelected by remember { mutableStateOf("Moderate") }
    var dietSelected by remember { mutableStateOf("Vegetarian") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = VitalityGreen,
                    trackColor = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Step 2 of 5", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                    Text(text = "Personal Info", style = MaterialTheme.typography.labelSmall, color = VitalityGreen)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Profile Setup", style = MaterialTheme.typography.headlineLarge, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InputPill("Age", age, { age = it }, modifier = Modifier.weight(1f))
                    GenderSelector("Gender", gender, { gender = it }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InputPill("Weight (kg)", weight, { weight = it }, modifier = Modifier.weight(1f))
                    InputPill("Height (cm)", height, { height = it }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                InputPill("Goal Weight (kg)", goalWeight, { goalWeight = it }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "What is your goal?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                GoalCard("Weight Loss", "Focus on fat burning", goalSelected == "Weight Loss") { goalSelected = "Weight Loss" }
                Spacer(modifier = Modifier.height(12.dp))
                GoalCard("Muscle Gain", "Focus on muscle building", goalSelected == "Muscle Gain") { goalSelected = "Muscle Gain" }
                Spacer(modifier = Modifier.height(12.dp))
                GoalCard("Maintenance", "Focus on healthy living", goalSelected == "Maintenance") { goalSelected = "Maintenance" }

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Activity Level", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                ActivityLevelRow(activitySelected) { activitySelected = it }

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Dietary Preference", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                DietCard(
                    emoji = "🥦",
                    title = "Vegetarian",
                    subtitle = "Plant-based, no meat or fish",
                    isSelected = dietSelected == "Vegetarian"
                ) { dietSelected = "Vegetarian" }
                Spacer(modifier = Modifier.height(12.dp))
                DietCard(
                    emoji = "🍗",
                    title = "Non-Vegetarian",
                    subtitle = "Includes meat, fish & poultry",
                    isSelected = dietSelected == "Non-Vegetarian"
                ) { dietSelected = "Non-Vegetarian" }
                Spacer(modifier = Modifier.height(12.dp))
                DietCard(
                    emoji = "🌱",
                    title = "Vegan",
                    subtitle = "No animal products at all",
                    isSelected = dietSelected == "Vegan"
                ) { dietSelected = "Vegan" }

                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = {
                        viewModel.onboard(
                            height = height.toFloatOrNull() ?: 170f,
                            weight = weight.toFloatOrNull() ?: 70f,
                            goalWeight = goalWeight.toFloatOrNull() ?: 65f,
                            activity = activitySelected,
                            goal = goalSelected,
                            diet = dietSelected,
                            onComplete = onComplete
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalityGreen),
                    enabled = !viewModel.isLoading,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Continue", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun InputPill(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VitalityGreen,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.White)
        )
    }
}

@Composable
fun GenderSelector(label: String, selectedGender: String, onGenderChange: (String) -> Unit, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Male", "Female", "Other")

    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().height(56.dp).clickable { expanded = true }
            ) {
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(text = selectedGender, fontSize = 16.sp, color = Color.White)
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                genders.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender, color = Color.White) },
                        onClick = {
                            onGenderChange(gender)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(title: String, subtitle: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, VitalityGreen) else null,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (isSelected) VitalityGreen else Color.White)
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
        }
    }
}

@Composable
fun ActivityLevelRow(selected: String, onSelect: (String) -> Unit) {
    val levels = listOf("Beginner", "Moderate", "Active")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        levels.forEach { level ->
            Surface(
                modifier = Modifier.weight(1f).clickable { onSelect(level) },
                shape = RoundedCornerShape(12.dp),
                color = if (selected == level) VitalityGreen else MaterialTheme.colorScheme.surface
            ) {
                Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                    Text(text = level, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (selected == level) Color.Black else Color.White)
                }
            }
        }
    }
}

@Composable
fun DietCard(emoji: String, title: String, subtitle: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, VitalityGreen) else null,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) VitalityGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 22.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isSelected) VitalityGreen else Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (isSelected) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = VitalityGreen,
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("✓", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}
