package ai.llm.config

import dev.langchain4j.web.search.WebSearchEngine
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine
import jakarta.enterprise.context.Dependent
import jakarta.enterprise.inject.Produces
import jakarta.inject.Named

@Dependent
class SearchEngine {

    @Produces
    @Named("googleEngine")
    fun googleEngine(): WebSearchEngine? {
        return GoogleCustomWebSearchEngine.builder()
            .apiKey("AIzaSyCXiCNXAcKxsjnte0ebe5FQZwlceGjLUvU")
            .csi("f009fbd9a12af4ccb")
            .logRequests(true)
            .logResponses(true)
            .maxRetries(3)
            .build();
    }
}
