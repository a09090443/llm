package ai.llm.tool

import ai.llm.util.time.DateTimeUtils
import dev.langchain4j.agent.tool.Tool
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class SystemTools {

    @Tool("取得現在時間")
    fun getCurrentTime() = DateTimeUtils.getDateNow(DateTimeUtils.dateTimeFormate1)
}
