package ai.llm.assistant

import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.output.Response
import dev.langchain4j.service.SystemMessage
import io.quarkiverse.langchain4j.RegisterAiService

@RegisterAiService(modelName = "code")
interface CodeAssistant {

    @SystemMessage("""
        你是一名專業程式設計師，非常了解現在軟體和硬體領域的所有程式語言，你只能回覆該領域的知識，如果輸入的問題是關於特定程式語言的（例如：Python、JavaScript、C#等），就只回答該程式語言的相關信息；
        超出領域問題只回答『我無法回答該問題，因為這不在我的專業範圍內。』
        """)
    fun chat(message: String): Response<AiMessage>
}
