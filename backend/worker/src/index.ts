interface Env {
  GEMINI_API_KEY: string;
  NOVA_CLIENT_TOKEN: string;
}

type ChatMessage = { role: "user" | "model"; content: string };

function json(data: unknown, status = 200): Response {
  return new Response(JSON.stringify(data), {
    status,
    headers: { "content-type": "application/json; charset=utf-8" }
  });
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    if (request.method === "OPTIONS") {
      return new Response(null, {
        status: 204,
        headers: {
          "access-control-allow-origin": "*",
          "access-control-allow-methods": "POST, OPTIONS",
          "access-control-allow-headers": "content-type, authorization"
        }
      });
    }

    const url = new URL(request.url);
    if (url.pathname !== "/v1/chat" || request.method !== "POST") {
      return json({ error: "Not found" }, 404);
    }

    // Temporary setup gate for initial testing. Replace with real user
    // authentication and abuse controls before a public production release.
    const suppliedToken = request.headers.get("authorization") ?? "";
    if (!env.NOVA_CLIENT_TOKEN || suppliedToken !== `Bearer ${env.NOVA_CLIENT_TOKEN}`) {
      return json({ error: "Unauthorized" }, 401);
    }
    if (!env.GEMINI_API_KEY) {
      return json({ error: "AI provider is not configured" }, 503);
    }

    let body: { messages?: ChatMessage[] };
    try {
      body = await request.json() as { messages?: ChatMessage[] };
    } catch {
      return json({ error: "Invalid JSON body" }, 400);
    }

    const messages = body.messages;
    if (!Array.isArray(messages) || messages.length === 0 || messages.length > 20) {
      return json({ error: "Send between 1 and 20 messages" }, 400);
    }

    const contents = [];
    for (const message of messages) {
      if (
        !message ||
        (message.role !== "user" && message.role !== "model") ||
        typeof message.content !== "string" ||
        message.content.trim().length === 0 ||
        message.content.length > 8000
      ) {
        return json({ error: "A message is invalid or too long" }, 400);
      }
      contents.push({
        role: message.role,
        parts: [{ text: message.content.trim() }]
      });
    }

    try {
      const upstream = await fetch(
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent",
        {
          method: "POST",
          headers: {
            "content-type": "application/json",
            "x-goog-api-key": env.GEMINI_API_KEY
          },
          body: JSON.stringify({
            systemInstruction: {
              parts: [{ text: "You are NOVA, a helpful, clear, friendly AI assistant. Give accurate answers and explain difficult things simply." }]
            },
            contents,
            generationConfig: { temperature: 0.7, maxOutputTokens: 2048 }
          })
        }
      );

      const result = await upstream.json() as {
        candidates?: Array<{ content?: { parts?: Array<{ text?: string }> } }>;
        error?: { message?: string };
      };

      if (!upstream.ok) {
        return json({ error: result.error?.message ?? "The AI provider request failed" }, 502);
      }

      const reply = result.candidates?.[0]?.content?.parts
        ?.map(part => part.text ?? "")
        .join("")
        .trim();

      if (!reply) return json({ error: "The AI returned an empty response" }, 502);
      return json({ reply });
    } catch {
      return json({ error: "Could not reach the AI provider. Please try again." }, 502);
    }
  }
};
