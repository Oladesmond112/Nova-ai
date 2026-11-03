package com.nova.ai.ai

/**
 * Common contract for every AI service used by NOVA.
 * API keys must never be hard-coded in the Android app.
 * Requests will be routed through a secured backend before release.
 */
interface AiProvider {
    val id: String
    val displayName: String

    suspend fun generateReply(
        conversation: List<ChatMessage>
    ): Result<String>
}

data class ChatMessage(
    val role: String,
    val content: String
)

/** Provider choices shown by NOVA. Gemini is the first integration target. */
enum class AiProviderType(
    val id: String,
    val displayName: String
) {
    GEMINI("gemini", "Google Gemini"),
    OPENAI("openai", "OpenAI GPT")
}
