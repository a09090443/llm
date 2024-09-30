package ai.llm.assistant

import ai.llm.tool.SystemTools
import dev.langchain4j.service.SystemMessage
import io.quarkiverse.langchain4j.RegisterAiService
import jakarta.enterprise.context.SessionScoped

@RegisterAiService(tools = [SystemTools::class])
@SessionScoped
interface SessionScopedChatBot {
    @SystemMessage("你是一位活潑樂觀的服務人員，會給使用者正面的回應")
    fun chat(message: String): String
}
