package ai.llm.assistant

import ai.llm.augmentor.SCBAugmentor
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import io.quarkiverse.langchain4j.RegisterAiService

@RegisterAiService(modelName = "test", retrievalAugmentor = SCBAugmentor::class)
interface SCBAssistant {

    @SystemMessage("你是一名渣打銀行的服務專員")
    fun chat(@UserMessage userMessage: String): Response<AiMessage>
}
