package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.VitalityFlowTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.DashboardScreen
import com.example.myapplication.ui.screens.LoginScreen
import com.example.myapplication.ui.screens.SignUpScreen

import com.example.myapplication.ui.screens.MealDetailsScreen
import com.example.myapplication.ui.screens.MealPlanScreen
import com.example.myapplication.ui.screens.WorkoutScreen
import com.example.myapplication.ui.screens.ProfileSetupScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitalityFlowTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClick = { navController.navigate("signup") }
                        )
                    }
                    composable("signup") {
                        SignUpScreen(
                            onNavigateToLogin = { navController.popBackStack() },
                            onRegisterSuccess = { 
                                navController.navigate("profile_setup") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("profile_setup") {
                        ProfileSetupScreen(
                            onComplete = {
                                navController.navigate("dashboard") {
                                    popUpTo("profile_setup") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("dashboard") {
                        DashboardScreen(
                            onMealPlanClick = { navController.navigate("meal_plan") },
                            onWorkoutClick = { navController.navigate("workout") },
                            onProfileClick = { navController.navigate("profile") }
                        )
                    }
                    composable("meal_plan") {
                        MealPlanScreen(
                             onMealClick = { navController.navigate("meal_details") },
                             onBack = { navController.popBackStack() }
                        )
                    }
                    composable("meal_details") {
                        MealDetailsScreen(onBack = { navController.popBackStack() })
                    }
                    composable("workout") {
                        WorkoutScreen()
                    }
                    composable("profile") {
                        val authViewModel: AuthViewModel = viewModel()
                        com.example.myapplication.ui.screens.ProfileScreen(
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                authViewModel.logout()
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VitalityFlowTheme {
        Greeting("Vitality Flow")
    }
}