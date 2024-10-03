package ai.llm.config

import ai.llm.model.SearchEngineConfig
import dev.langchain4j.web.search.WebSearchEngine
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine
import jakarta.enterprise.context.Dependent
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject
import jakarta.inject.Named
import kotlin.jvm.optionals.getOrNull

@Dependent
class SearchEngine {

    @Inject
    lateinit var searchEngineConfig: SearchEngineConfig

    @Produces
    @Named("googleEngine")
    fun googleEngine(): WebSearchEngine? {
        return GoogleCustomWebSearchEngine.builder()
            .apiKey(searchEngineConfig.apiKey().getOrNull())
            .csi(searchEngineConfig.csi().getOrNull())
            .logRequests(true)
            .logResponses(true)
            .maxRetries(3)
            .build();
    }
}
