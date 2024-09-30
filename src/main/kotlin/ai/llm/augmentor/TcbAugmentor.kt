package ai.llm.augmentor

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.rag.DefaultRetrievalAugmentor
import dev.langchain4j.rag.RetrievalAugmentor
import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer
import dev.langchain4j.rag.query.transformer.QueryTransformer
import dev.langchain4j.store.embedding.EmbeddingStore
import io.quarkiverse.langchain4j.ModelName
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import java.util.function.Supplier

@ApplicationScoped
class TcbAugmentor(@ModelName("test") var ollamaModel: ChatLanguageModel,
                   @ModelName("test") var embeddingModel: EmbeddingModel,
                   @Named("tcbMilvus") private val tcbMilvusStore: EmbeddingStore<TextSegment>) : Supplier<RetrievalAugmentor> {
    private lateinit var augmentor: RetrievalAugmentor

    init {

        val queryTransformer: QueryTransformer = CompressingQueryTransformer(ollamaModel)

        val contentRetriever: ContentRetriever = EmbeddingStoreContentRetriever.builder()
            .embeddingStore(tcbMilvusStore)
            .embeddingModel(embeddingModel)
            .maxResults(3)
            .minScore(0.6)
            .build()

        augmentor = DefaultRetrievalAugmentor
            .builder()
            .queryTransformer(queryTransformer)
            .contentRetriever(contentRetriever)
            .build()
    }

    override fun get(): RetrievalAugmentor {
        return augmentor
    }
}
