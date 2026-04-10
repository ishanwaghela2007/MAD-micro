# 🌿 Vitality Flow — Android Frontend

> A premium AI-powered fitness & nutrition Android application built with Kotlin and Jetpack Compose, integrated with the Vitality Flow Go microservices backend.

---

## 📱 Overview

**Vitality Flow** is a modern health and fitness app that leverages AI to generate personalized meal plans and workout routines. The app features a sleek dark-themed UI with neon green accents, smooth navigation, and real-time data from a Go-based microservices backend.

---

## ✨ Features

- 🔐 **Authentication** — Email/password login, registration, and Google OAuth sign-in support
- 🧭 **Onboarding** — Guided profile setup (height, weight, activity level, fitness goals, dietary preferences)
- 🏠 **Dashboard** — Personalized home screen with quick access to AI-generated plans
- 🥗 **AI Meal Planner** — Generate and view personalized daily meal plans with nutrient breakdown
- 🍽️ **Meal Details** — Deep-dive into individual meals
- 💪 **AI Workout Planner** — Generate custom workout plans with exercise sets, reps, and intensity
- 👤 **Profile Screen** — View personal stats and securely log out (JWT blacklisted on backend)

---

## 🏗️ Architecture

The project follows a clean, layered architecture:

```
com.example.myapplication/
├── MainActivity.kt              # App entry point & Navigation graph
├── data/
│   ├── network/
│   │   ├── VitalityApiService.kt   # Retrofit API interface & request/response models
│   │   ├── NetworkModule.kt        # OkHttp + Retrofit setup with auth interceptor
│   │   └── TokenManager.kt        # JWT token storage & retrieval
│   ├── model/                      # (Data models)
│   └── repository/                 # (Repository layer)
└── ui/
    ├── screens/
    │   ├── LoginScreen.kt
    │   ├── SignUpScreen.kt
    │   ├── ProfileSetupScreen.kt
    │   ├── DashboardScreen.kt
    │   ├── MealPlanScreen.kt
    │   ├── MealDetailsScreen.kt
    │   ├── WorkoutScreen.kt
    │   └── ProfileScreen.kt
    ├── viewmodel/
    │   └── AuthViewModel.kt
    └── theme/
        └── Theme.kt               # VitalityFlowTheme — dark mode with neon green accents
```

---

## 🗺️ Navigation Graph

```
login ──► dashboard ──► meal_plan ──► meal_details
  │            │
  │            ├──► workout
  │            └──► profile ──► (logout → login)
  │
  └──► signup ──► profile_setup ──► dashboard
```

---

## 🔌 API Endpoints

| Method | Endpoint              | Description                        |
|--------|-----------------------|------------------------------------|
| POST   | `auth/login/email`    | Email + password login             |
| POST   | `auth/login`          | OAuth (Google) login               |
| POST   | `auth/register`       | New user registration              |
| POST   | `auth/logout`         | Logout (JWT blacklisted in Redis)  |
| GET    | `profile`             | Fetch authenticated user profile   |
| POST   | `profile/onboard`     | Submit onboarding data             |
| POST   | `meals/generate`      | Generate AI meal plan              |
| POST   | `workouts/generate`   | Generate AI workout plan           |

---

## 🛠️ Tech Stack

| Category          | Technology                              |
|-------------------|-----------------------------------------|
| Language          | Kotlin                                  |
| UI Framework      | Jetpack Compose + Material 3            |
| Navigation        | Navigation Compose `2.8.5`              |
| Networking        | Retrofit `2.11.0` + OkHttp `4.12.0`    |
| JSON Parsing      | Gson (Retrofit converter)               |
| Image Loading     | Coil `2.7.0`                            |
| Auth              | Google Play Services Auth `21.2.0`      |
| ViewModel         | Lifecycle ViewModel Compose             |
| Min SDK           | API 24 (Android 7.0 Nougat)            |
| Target SDK        | API 36                                  |
| AGP               | 9.1.0                                   |
| Kotlin            | 2.2.10                                  |

---

## ⚙️ Prerequisites

- **Android Studio** Hedgehog or later (with Compose support)
- **JDK 11**
- **Android SDK** with API 24–36 installed
- The **Vitality Flow backend** running and reachable (see backend repo)

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/ishanwaghela2007/MAD-micro.git
cd MAD-micro
```

### 2. Configure the Backend URL

Open `app/src/main/java/com/example/myapplication/data/network/NetworkModule.kt` and update the base URL to point to your running backend gateway:

```kotlin
private const val BASE_URL = "http://<YOUR_BACKEND_HOST>:<PORT>/"
```

> **Note:** For a physical Android device, use your machine's LAN IP (e.g., `192.168.x.x`), **not** `localhost`.  
> For an emulator, use `http://10.0.2.2:<PORT>/`.

### 3. Build & Run

Open the project in **Android Studio**, sync Gradle, then click **Run ▶** on a connected device or emulator (API 24+).

Alternatively, build from the terminal:

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🌐 Network & Security

- All API requests are sent over HTTP (cleartext traffic enabled for local development — see `AndroidManifest.xml`).
- JWT access tokens are stored via `TokenManager` and attached as `Authorization: Bearer <token>` headers by an OkHttp interceptor defined in `NetworkModule.kt`.
- Logout calls `POST auth/logout`, which blacklists the JWT in the backend's Redis store.

> **⚠️ Production Note:** Before releasing, disable `android:usesCleartextTraffic="true"` in `AndroidManifest.xml` and switch to HTTPS.

---

## 📦 Key Dependencies

```toml
# gradle/libs.versions.toml

[versions]
composeBom          = "2024.09.00"
navigationCompose   = "2.8.5"
retrofit            = "2.11.0"
okhttp              = "4.12.0"
coil                = "2.7.0"
playServicesAuth    = "21.2.0"
```

---

## 📂 Related Repositories

- 🔗 **Backend (Go Microservices):** [MAD-micro — backend services](https://github.com/ishanwaghela2007/MAD-micro)

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "feat: add your feature"`
4. Push to the branch: `git push origin feature/your-feature-name`
5. Open a Pull Request

---

## 📄 License

This project is for educational/MAD coursework purposes.

---

*Built with ❤️ using Kotlin & Jetpack Compose*
