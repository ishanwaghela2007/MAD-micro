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
import com.example.myapplication.ui.theme.VitalityGreen

@Composable
fun ProfileSetupScreen(onComplete: () -> Unit) {
    var age by remember { mutableStateOf("24") }
    var weight by remember { mutableStateOf("72") }
    var goalSelected by remember { mutableStateOf("Weight Loss") }
    var activitySelected by remember { mutableStateOf("Moderate") }

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
                    InputPill("Age", age, Modifier.weight(1f))
                    InputPill("Gender", "Male", Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    InputPill("Weight (kg)", weight, Modifier.weight(1f))
                    InputPill("Height (cm)", "178", Modifier.weight(1f))
                }
                
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

                Spacer(modifier = Modifier.height(48.dp))
                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VitalityGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Continue", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun InputPill(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = value, fontSize = 16.sp, color = Color.White)
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
