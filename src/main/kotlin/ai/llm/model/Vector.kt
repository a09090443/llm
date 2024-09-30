package ai.llm.model

import io.quarkus.runtime.annotations.ConfigDocMapKey
import io.quarkus.runtime.annotations.ConfigGroup
import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithDefault
import io.smallrye.config.WithParentName
import java.util.*

@ConfigMapping(prefix = "vector")
interface Vector {

    @WithParentName
    fun defaultConfig(): BaseConfig?

    @WithParentName
    @ConfigDocMapKey("vector-name")
    fun namedConfig(): Map<String, BaseConfig?>?

    interface BaseConfig {
        fun chroma(): Chroma
        fun milvus(): Milvus

    }

    @ConfigGroup
    interface Chroma : CommonConfig

    @ConfigGroup
    interface Milvus : CommonConfig {
        @WithDefault("1024")
        fun dimension(): Int
    }

    @ConfigGroup
    interface CommonConfig {

        @WithDefault("localhost")
        fun collectionName(): String?

        @WithDefault("localhost")
        fun host(): String?

    }
}
