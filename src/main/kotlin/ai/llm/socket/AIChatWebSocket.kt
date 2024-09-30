package ai.llm.socket

import ai.llm.assistant.SessionScopedChatBot
import io.quarkus.websockets.next.OnOpen
import io.quarkus.websockets.next.OnTextMessage
import io.quarkus.websockets.next.WebSocket
import io.quarkus.websockets.next.WebSocketConnection

@WebSocket(path = "/ai/chat")
class AIChatWebSocket(
    var sessionScopedChatBot: SessionScopedChatBot,
    var connection: WebSocketConnection
) {
    @OnOpen(broadcast = true)
    fun onOpen(): String {
        return "你好!歡迎來到客服聊天室，我可以幫你回答甚麼問題呢?"
    }

    @OnTextMessage
    fun onMessage(message: String):String {
        connection.broadcast().sendTextAndAwait(message);
        return sessionScopedChatBot.chat(message)
    }
}
