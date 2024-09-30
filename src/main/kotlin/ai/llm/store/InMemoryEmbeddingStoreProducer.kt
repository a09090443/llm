package ai.llm.store

import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import io.quarkiverse.langchain4j.ModelName
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject

class InMemoryEmbeddingStoreProducer {
    @Inject
    @ModelName("test")
    lateinit var embeddingModel: EmbeddingModel

    @Produces
    @ApplicationScoped
    fun getStore(): InMemoryEmbeddingStore<TextSegment> {
        val store: InMemoryEmbeddingStore<TextSegment> = InMemoryEmbeddingStore<TextSegment>()
//        embed(store, "2024年台灣新總統是賴清德")

        return store
    }

    private fun embed(store: InMemoryEmbeddingStore<TextSegment>, text: String) {
        store.add(embeddingModel.embed(text).content(), TextSegment(text, Metadata()))
    }
}
