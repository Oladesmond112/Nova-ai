# NOVA AI

NOVA AI is an Android-first AI assistant. The first release focuses on a clean chat experience, with room to add image, voice, and creative tools later.

## AI provider strategy

- **First integration target:** Google Gemini.
- **Designed for multiple providers:** OpenAI GPT and other providers can be added behind the same `AiProvider` interface.
- **Security:** Never place a private AI API key directly in the Android source code or ship it inside the APK. The production app will call a secured backend that holds provider credentials.
- **Current status:** The interface foundation is in place. A live AI service is not connected yet; backend configuration and a real device build/test are still required.

## Android project

- Kotlin
- Jetpack Compose + Material 3
- Minimum Android version: API 23
- Java 17

## Development

The Android debug build is configured through GitHub Actions. Open the **Actions** tab and select the Android debug build workflow to inspect its latest run and download its APK artifact if the run succeeds.

## Roadmap

1. Complete and verify the Android debug build.
2. Set up a secure backend for Gemini requests.
3. Connect chat history and streaming responses.
4. Add provider switching, then integrate OpenAI GPT.
5. Add voice and image features.
