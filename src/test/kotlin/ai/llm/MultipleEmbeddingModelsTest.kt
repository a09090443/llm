package ai.llm

import dev.langchain4j.model.embedding.EmbeddingModel
import io.quarkiverse.langchain4j.ModelName
import io.quarkiverse.langchain4j.ollama.OllamaEmbeddingModel
import io.quarkus.arc.ClientProxy
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@QuarkusTest
class MultipleEmbeddingModelsTest {
    @field:Inject
    @field:ModelName("test")
    lateinit var ollamaEmbeddingModel: EmbeddingModel

    @Test
    fun thirdNamedModel() {
        assertThat(ClientProxy.unwrap(ollamaEmbeddingModel)).isInstanceOf(OllamaEmbeddingModel::class.java)
    }
}
