package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.network.MealPlanResponse
import com.example.myapplication.data.network.NetworkModule
import com.example.myapplication.data.network.ProfileResponse
import com.example.myapplication.data.network.WorkoutResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiService = NetworkModule.apiService

    var profile by mutableStateOf<ProfileResponse?>(null)
    var mealPlan by mutableStateOf<MealPlanResponse?>(null)
    var workout by mutableStateOf<WorkoutResponse?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun fetchProfile() {
        viewModelScope.launch {
            isLoading = true
            try {
                profile = apiService.getProfile()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchMealPlan() {
        viewModelScope.launch {
            isLoading = true
            try {
                mealPlan = apiService.generateMealPlan()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchWorkout() {
        viewModelScope.launch {
            isLoading = true
            try {
                workout = apiService.generateWorkout()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun onboard(height: Float, weight: Float, goalWeight: Float, activity: String, goal: String, diet: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                apiService.onboard(com.example.myapplication.data.network.OnboardRequest(
                    heightCm = height,
                    currentWeightKg = weight,
                    goalWeightKg = goalWeight,
                    activityLevel = activity,
                    fitnessGoal = goal,
                    dietaryPreference = diet
                ))
                onComplete()
            } catch (e: Exception) {
                error = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
