# BGMI Performance Optimizer

A standalone Android companion app for BGMI players. It provides device-aware performance guidance, safe graphics and sensitivity presets, gyroscope reference settings, and device status information.

## Important limits

- This app does not modify BGMI files, memory, network traffic, or anti-cheat systems.
- It does not inject aim assistance or remove recoil. It provides configurable in-game sensitivity recommendations only.
- FPS options shown are targets, not unlocks. Actual frame rates depend on BGMI's supported device profile, game version, thermal state, and display settings.
- The initial device profile targets the OnePlus 9 (Snapdragon 888, Adreno 660, 120 Hz display).

## Build

Open this project in Android Studio (Giraffe or newer) and sync Gradle. Select **Build > Build APK(s)**. The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

Package: `com.performance.bgmioptimizer`
