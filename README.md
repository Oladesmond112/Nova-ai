# NOVA AI

NOVA AI is an Android-first creative assistant. The long-term product vision brings AI chat, image generation, video creation, animation, AI writing, and voice assistance into one workspace.

## Android app

The first Android foundation is built with:
- Kotlin
- Jetpack Compose
- Material 3
- Gradle Kotlin DSL

### Current status

The repository now contains the initial Android app structure and a first home-screen prototype. The tool cards and message field are currently UI only; AI services and real message handling have not been connected yet.

## Build

Requirements:
- JDK 17
- Android SDK 36
- Gradle 8.13

From the repository root, run:

```bash
gradle assembleDebug
```

The debug APK is generated at:

```
app/build/outputs/apk/debug/app-debug.apk
```

A GitHub Actions workflow also builds the debug APK on pushes and pull requests and uploads it as a workflow artifact.

## Project structure

```
.
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/novaai/app/MainActivity.kt
├── build.gradle.kts
├── gradle.properties
└── settings.gradle.kts
```

## Roadmap

1. Finish and validate the Android foundation.
2. Build the chat experience and conversation history.
3. Connect a secure AI backend (never place API keys in the Android app).
4. Add image, writing, voice, and video tools in stages.
5. Add sign-in, user settings, and release packaging.
