# NS Navigation Gesture

**NS Navigation Gesture** is a modern, lightweight Android application built with **Jetpack Compose** and **Kotlin** that brings customizable full-screen edge-swipe gesture navigation to your Android device.

---

## 🚀 Features

- **Customizable Gesture Zones**: Configure individual actions for:
  - Left Edge Swipe
  - Right Edge Swipe
  - Bottom Edge Swipe
  - Bottom Hold Gesture
- **Wide Range of Actions**: Trigger Back, Home, Recent Apps, Notifications, Quick Settings, Split Screen, Power Dialog, Lock Screen, and Screenshots.
- **System Accessibility Integration**: Powered by Android's Accessibility Service API to ensure smooth gesture detection across portrait and landscape orientations.
- **Haptic Feedback**: Tactile vibration feedback on gesture activation.
- **Customizable Visual Bars**: Toggle visibility of gesture indicator bars along screen edges.
- **Material 3 UI**: Clean, modern interface supporting dynamic light/dark themes and Edge-to-Edge layouts.

---

## 🛠️ Tech Stack & Architecture

- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
- **Architecture**: MVVM (Model-View-ViewModel) pattern.
- **Core APIs**: 
  - `AccessibilityService` for global touch interception and system action execution.
  - `SharedPreferences` / Custom Repository for persistent preference management.
  - `WindowManager` for floating edge visualizer bars.
- **Language**: 100% Kotlin.

---

## 📱 Permissions Required

To function correctly, the app requires the following permission:
- **Accessibility Service (`BIND_ACCESSIBILITY_SERVICE`)**: Used solely to detect edge gestures and execute global navigation actions (Home, Back, Recents, etc.). No personal or sensitive data is collected.
- **Vibrate (`VIBRATE`)**: Provides haptic feedback when gestures are successfully triggered.

---

## 🏗️ Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/ns-navigation-gesture.git
   ```
2. Open the project in [Android Studio](https://developer.android.com/studio) (Koala or newer recommended).
3. Sync project with Gradle files.
4. Connect an Android device (running Android 7.0 / API 24 or higher) or start an emulator.
5. Run the app (`:app:assembleDebug`).

---

## 📂 Project Structure

```text
com.nscreation.navigationgesture/
│
├── components/          # Reusable UI components (e.g., ActionDropdownCard)
├── modules/
│   ├── gesture/         # Gesture feature module
│   │   ├── data/        # Repository & Preferences storage
│   │   ├── domain/      # Domain models (GestureAction)
│   │   └── presentation/# MainScreen & MainViewModel
│   └── welcome/         # Welcome / onboarding screen
│
├── service/             # GestureAccessibilityService implementation
├── ui/theme/            # Material 3 Theme, Colors, and Typography
└── MainActivity.kt      # App entry point
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
