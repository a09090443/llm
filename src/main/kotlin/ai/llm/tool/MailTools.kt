package ai.llm.tool

import ai.llm.util.time.DateTimeUtils
import dev.langchain4j.agent.tool.Tool
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class MailTools {

    @Tool("取得現在時間")
    fun sendMail() = DateTimeUtils.getDateNow(DateTimeUtils.dateTimeFormate1)
}
