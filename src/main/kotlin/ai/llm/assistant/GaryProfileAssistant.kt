package ai.llm.assistant

import ai.llm.augmentor.ProfileAugmentor
import dev.langchain4j.service.MemoryId
import dev.langchain4j.service.UserMessage
import io.quarkiverse.langchain4j.RegisterAiService
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@RegisterAiService(modelName = "test", retrievalAugmentor = ProfileAugmentor::class)
interface GaryProfileAssistant {

    fun chat(@MemoryId memoryId: String, @UserMessage message: String): String

}
