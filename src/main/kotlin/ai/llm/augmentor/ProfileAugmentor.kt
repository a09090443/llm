package ai.llm.augmentor

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.rag.DefaultRetrievalAugmentor
import dev.langchain4j.rag.RetrievalAugmentor
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import jakarta.enterprise.context.ApplicationScoped
import java.util.function.Supplier

@ApplicationScoped
class ProfileAugmentor(store: InMemoryEmbeddingStore<TextSegment>) : Supplier<RetrievalAugmentor> {
    private lateinit var augmentor: RetrievalAugmentor

    init {

        val document = loadDocument("D:/doc/profile/profile.txt")
        EmbeddingStoreIngestor.ingest(document, store)
        val contentRetriever = EmbeddingStoreContentRetriever.builder()
            .embeddingStore(store)
            .maxResults(3)
            .build()

//        val contentRetriever = EmbeddingStoreContentRetriever.from(store)

        augmentor = DefaultRetrievalAugmentor
            .builder()
            .contentRetriever(contentRetriever)
            .build()
    }

    override fun get(): RetrievalAugmentor {
        return augmentor
    }
}
