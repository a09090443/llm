package ai.llm.assistant

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import io.quarkiverse.langchain4j.RegisterAiService

@RegisterAiService(modelName="test")
interface PoemWriter {

    @SystemMessage("你是一位專業的詩人")
    @UserMessage(
        """
    寫一首詩詞關於{topic}. 這首詩詞需有{lines}行
"""
    )
    fun writeAPoem(topic: String, lines: Int): String
}
