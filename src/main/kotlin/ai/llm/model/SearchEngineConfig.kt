package ai.llm.model

import io.smallrye.config.ConfigMapping
import java.util.*

@ConfigMapping(prefix = "search-engine")
interface SearchEngineConfig {

    fun apiKey(): Optional<String>

    fun csi(): Optional<String>
}
