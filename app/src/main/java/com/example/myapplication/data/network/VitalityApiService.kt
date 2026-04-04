package com.example.myapplication.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.http.*

interface VitalityApiService {
    @POST("auth/login/email")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @POST("profile/onboard")
    suspend fun onboard(@Body request: OnboardRequest): MessageResponse

    @POST("meals/generate")
    suspend fun generateMealPlan(): MealPlanResponse
}

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("device_id") val deviceId: String = "android_123",
    @SerializedName("platform") val platform: String = "android"
)

data class AuthResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("user_id") val userId: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)

data class RegisterResponse(
    @SerializedName("user_id") val userId: String
)

data class OnboardRequest(
    @SerializedName("height_cm") val heightCm: Float,
    @SerializedName("current_weight_kg") val currentWeightKg: Float,
    @SerializedName("goal_weight_kg") val goalWeightKg: Float,
    @SerializedName("activity_level") val activityLevel: String,
    @SerializedName("fitness_goal") val fitnessGoal: String,
    @SerializedName("dietary_preference") val dietaryPreference: String
)

data class ProfileResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("current_weight_kg") val currentWeightKg: Float,
    @SerializedName("fitness_goal") val fitnessGoal: String
)

data class MessageResponse(
    @SerializedName("message") val message: String
)

data class MealPlanResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("daily_plan") val dailyPlan: String, // Markdon or JSON string from AI
    @SerializedName("nutrients") val nutrients: NutrientDetails
)

data class NutrientDetails(
    @SerializedName("protein") val protein: Float,
    @SerializedName("carbs") val carbs: Float,
    @SerializedName("fats") val fats: Float,
    @SerializedName("calories") val calories: Float
)
