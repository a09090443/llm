package ai.llm.config

import dev.langchain4j.memory.chat.ChatMemoryProvider
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore
import java.util.function.Supplier

class CustomProvider : Supplier<ChatMemoryProvider?> {
    private val store = InMemoryChatMemoryStore()

    override fun get(): ChatMemoryProvider {
        return ChatMemoryProvider { memoryId ->
            MessageWindowChatMemory.builder()
                .maxMessages(20)
                .id(memoryId)
                .chatMemoryStore(store)
                .build()
        }
    }
}
