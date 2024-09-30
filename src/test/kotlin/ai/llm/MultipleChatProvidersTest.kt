package ai.llm

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.openai.OpenAiChatModel
import io.quarkiverse.langchain4j.ModelName
import io.quarkiverse.langchain4j.ollama.OllamaChatLanguageModel
import io.quarkiverse.langchain4j.ollama.OllamaStreamingChatLanguageModel
import io.quarkus.arc.ClientProxy
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusTest
class MultipleChatProvidersTest {
    @Inject
    lateinit var defaultModel: ChatLanguageModel

    @Inject
    @ModelName("test")
    lateinit var ollamaModel: ChatLanguageModel

    @Inject
    @ModelName("test")
    lateinit var ollamaStreamingChatLanguageModel: StreamingChatLanguageModel

    //    @Test
    fun defaultModel() {
        assertThat(ClientProxy.unwrap(defaultModel)).isInstanceOf(OpenAiChatModel::class.java)
    }

    @Test
    fun ollamaModel() {
        assertThat(ClientProxy.unwrap(ollamaModel)).isInstanceOf(OllamaChatLanguageModel::class.java)
    }

    @Test
    fun ollamaStreamingModel() {
        assertThat(ClientProxy.unwrap(ollamaStreamingChatLanguageModel)).isInstanceOf(OllamaStreamingChatLanguageModel::class.java)
    }
}
