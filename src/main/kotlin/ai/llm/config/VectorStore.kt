package ai.llm.config

import ai.llm.model.Vector
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore
import jakarta.enterprise.context.Dependent
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject
import jakarta.inject.Named

@Dependent
class VectorStore {

    @Inject
    lateinit var vector: Vector

    @Produces
    @Named("chroma")
    fun chromaStore(): ChromaEmbeddingStore? {
        return vector.defaultConfig()?.chroma()?.let {
            ChromaEmbeddingStore
                .builder()
                .baseUrl(it.host())
                .collectionName(it.collectionName())
                .logRequests(true)
                .logResponses(true)
                .build()
        }
    }

    @Produces
    @Named("milvus")
    fun milvusStore(): MilvusEmbeddingStore? {
        return vector.defaultConfig()?.milvus()?.let {
            MilvusEmbeddingStore
                .builder()
                .uri(it.host())
                .collectionName(it.collectionName())
                .dimension(it.dimension())
                .build()
        }
    }

    @Produces
    @Named("tcbMilvus")
    fun webcommMilvusStore(): MilvusEmbeddingStore? {
        return vector.namedConfig()?.get("tcb")?.milvus()?.let {
            return MilvusEmbeddingStore.builder()
                .uri(it.host())
                .collectionName(it.collectionName())
                .dimension(1024)
                .build()
        } ?: throw IllegalStateException("Milvus configuration for 'tcb' is missing")
    }

    @Produces
    @Named("scbMilvus")
    fun scbMilvusStore(): MilvusEmbeddingStore? {
        return vector.namedConfig()?.get("scb")?.milvus()?.let {
            return MilvusEmbeddingStore.builder()
                .uri(it.host())
                .collectionName(it.collectionName())
                .dimension(1024)
                .build()
        } ?: throw IllegalStateException("Milvus configuration for 'scb' is missing")
    }

}
