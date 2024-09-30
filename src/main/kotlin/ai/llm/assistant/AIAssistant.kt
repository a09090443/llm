package ai.llm.assistant

import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.UserMessage
import io.quarkiverse.langchain4j.RegisterAiService
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@RegisterAiService(modelName = "test")
interface AIAssistant {

    fun chat(@MemoryId memoryId: String, @UserMessage userMessage: String): String
}
