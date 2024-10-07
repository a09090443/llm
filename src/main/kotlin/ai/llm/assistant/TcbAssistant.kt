package ai.llm.assistant

import ai.llm.augmentor.TcbAugmentor
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import io.quarkiverse.langchain4j.RegisterAiService

@RegisterAiService(modelName = "test", retrievalAugmentor = TcbAugmentor::class)
interface TcbAssistant {

    @SystemMessage("你是一名合庫金庫的服務專員")
    fun chat(@UserMessage userMessage: String): Response<AiMessage>
}
