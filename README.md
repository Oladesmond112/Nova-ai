# NOVA AI

NOVA AI is an Android-first AI assistant. The first release focuses on a clean chat experience, with room to add image, voice, and creative tools later.

## AI provider strategy

- **First integration target:** Google Gemini.
- **Designed for multiple providers:** OpenAI GPT and other providers can be added behind the same `AiProvider` interface.
- **Current status:** Android foundation and a Gemini backend starter are in the repository. The live service is not deployed or connected yet.

## Backend security

The backend starter is in `backend/worker` and is designed for Cloudflare Workers. It reads `GEMINI_API_KEY` and `NOVA_CLIENT_TOKEN` from encrypted Worker secrets; never commit real keys or put a Gemini key in the Android app.

The bearer token is a temporary setup gate, not strong end-user authentication: anything shipped inside an APK can be extracted. Before public release, replace it with real user authentication, abuse prevention, and per-user quotas. Review provider costs and limits before enabling public access.

## Android project

- Kotlin and Jetpack Compose + Material 3
- Minimum Android version: API 23
- Java 17
- Internet permission enabled for future API requests

## Backend setup (when ready)

1. Create a Cloudflare account and a Worker named `nova-ai-api`.
2. Deploy the files in `backend/worker` using Wrangler or the Cloudflare dashboard.
3. Add `GEMINI_API_KEY` and `NOVA_CLIENT_TOKEN` as **Worker secrets**, not plaintext variables.
4. Test the `POST /v1/chat` endpoint before connecting the Android client.
5. Configure Android with the deployed API URL without committing any private credentials.

Cloudflare Workers currently lists a Free plan allowance of 100,000 requests per day; Gemini usage may have separate quotas or charges. Check both services' current terms and pricing before launch.

## Build

The Android debug build is configured through GitHub Actions. Open the **Actions** tab and inspect the latest Android debug build run. Download the APK artifact only if the workflow succeeds.

## Roadmap

1. Deploy and test the backend.
2. Connect Android chat to the backend.
3. Add real user authentication and abuse protection before public release.
4. Add provider switching and OpenAI GPT.
5. Add voice and image features.
