package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
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
import com.example.myapplication.ui.theme.VitalityGreenDark

@Composable
fun WorkoutScreen() {
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
                WorkoutHeader()
                Spacer(modifier = Modifier.height(24.dp))
                FilterRow()
                Spacer(modifier = Modifier.height(24.dp))
                HeroWorkoutCard()
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Personalized Suggestions",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            items(sampleWorkouts) { workout ->
                WorkoutItemCard(workout)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun WorkoutHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Daily Suggestions", style = MaterialTheme.typography.headlineLarge, fontSize = 24.sp)
        }
        IconButton(onClick = {}, modifier = Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surface)) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.LightGray)
        }
    }
}

@Composable
fun FilterRow() {
    val filters = listOf("15 min", "HIIT", "Beg", "Yoga")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters) { filter ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (filter == "HIIT") VitalityGreen else MaterialTheme.colorScheme.surface,
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (filter == "HIIT") Color.Black else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun HeroWorkoutCard() {
    Surface(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().height(200.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?q=80&w=800",
                contentDescription = "Workout Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)) {
                Surface(color = VitalityGreen.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp)) {
                    Text(text = "LIVE SESSION", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
                Text(text = "Intense Full Body", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Text(text = "45 mins \u2022 Advanced", color = Color.LightGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun WorkoutItemCard(workout: WorkoutModel) {
    Surface(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = workout.imageUrl,
                contentDescription = workout.name,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = workout.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "${workout.sets} x ${workout.reps} \u2022 ${workout.intensity}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
            IconButton(
                onClick = {},
                modifier = Modifier.size(40.dp).clip(CircleShape).background(VitalityGreen)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.Black)
            }
        }
    }
}

data class WorkoutModel(val name: String, val sets: Int, val reps: String, val intensity: String, val imageUrl: String)
val sampleWorkouts = listOf(
    WorkoutModel("Diamond Pushups", 3, "12 reps", "Intense", "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=200"),
    WorkoutModel("Pistol Squats", 4, "8 reps", "Medium", "https://images.unsplash.com/photo-1574680096145-d05b474e2155?q=80&w=200"),
    WorkoutModel("Plank Rotations", 3, "45 sec", "Low", "https://images.unsplash.com/photo-1599058917212-d750089bc07e?q=80&w=200"),
    WorkoutModel("Mountain Climbers", 3, "30 reps", "HIIT", "https://images.unsplash.com/photo-1518622358385-8ea7d0794bf6?q=80&w=200")
)
