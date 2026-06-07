# Black Screen

An AMOLED power-saving overlay app for Android (API 26+).

## Features

- **Floating bubble trigger** — a small persistent overlay button stays on top of all apps
- **Full-screen black overlay** — pure `#000000` canvas optimized for OLED/AMOLED displays
- **Double-tap to dismiss** — tap twice on the black screen to restore normal visibility
- **Foreground service** — keeps the overlay alive system-wide with a minimal notification
- **Material 3 UI** — dark-themed setup screen with permission management

## How it works

1. Grant **Draw over other apps** permission
2. Grant **Post notifications** permission (Android 13+)
3. Tap **Start Overlay Service**
4. A small circle bubble appears on screen — tap it to activate the black screen
5. Double-tap the black screen to dismiss it

## Permissions

| Permission | Reason |
|---|---|
| `SYSTEM_ALERT_WINDOW` | Draw the floating bubble and black overlay over other apps |
| `FOREGROUND_SERVICE` | Keep the service running in the background |
| `FOREGROUND_SERVICE_SPECIAL_USE` | Required for overlay-type foreground services on API 34+ |
| `POST_NOTIFICATIONS` | Show the persistent service notification (Android 13+) |

## Build

```bash
./gradlew assembleDebug
```

Requires JDK 17. Output APK: `app/build/outputs/apk/debug/app-debug.apk`

## Tech stack

- Kotlin + Jetpack Compose
- `LifecycleService` + `WindowManager` for system overlays
- `ComposeView` hosted inside `WindowManager` for Compose-in-overlay
- Gradle 8.6 / AGP 8.4.1 / Kotlin 2.0.0

## Package

`com.willkez.darker`

## License

MIT
