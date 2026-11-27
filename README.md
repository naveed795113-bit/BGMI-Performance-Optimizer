# BGMI Performance Optimizer

A standalone Android gaming-session companion targeting the OnePlus 9.

## Current test build

- Launches the installed official BGMI app (`com.pubg.imobile`).
- Offers optional Android Do Not Disturb control after the user grants notification-policy access. The app saves and restores the previous interruption filter.
- Provides shortcuts to Android display and battery settings.
- Displays live device memory, battery temperature, thermal state and display refresh rate.
- Does not read or modify BGMI sensitivity, ADS, gyroscope, button layout or control size.

## Important technical limits

- This app does not inject code, modify BGMI files or memory, automate gameplay, bypass anti-cheat, or manipulate recoil/aim assist.
- A third-party app cannot force BGMI to unlock 120 FPS or restricted graphics, change BGMI's internal frame pacing, or guarantee zero lag/hot-drop stability.
- The phone's 120 Hz display refresh rate is not proof that BGMI is rendering at 120 FPS.
- Actual performance depends on BGMI's supported device profile, game version, thermal conditions, network quality and server load.
- The display and battery shortcuts open Android settings; they do not secretly force vendor-specific performance modes.

## Build

GitHub Actions builds a debug APK using Gradle 8.9 and Java 17. The APK is uploaded as the workflow artifact `bgmi-performance-optimizer-debug`.

Package: `com.performance.bgmioptimizer`
