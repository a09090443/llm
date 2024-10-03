package ai.llm.resource

import ai.llm.assistant.AIAssistant
import ai.llm.assistant.CodeAssistant
import ai.llm.assistant.GaryProfileAssistant
import ai.llm.assistant.PoemWriter
import ai.llm.assistant.SCBAssistant
import ai.llm.assistant.TcbAssistant
import ai.llm.enums.Company
import ai.llm.util.logger
import dev.langchain4j.agent.tool.ToolSpecifications
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.image.Image
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.ToolExecutionResultMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.StreamingResponseHandler
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.image.ImageModel
import dev.langchain4j.model.output.Response
import dev.langchain4j.store.embedding.EmbeddingStore
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore
import dev.langchain4j.web.search.WebSearchEngine
import dev.langchain4j.web.search.WebSearchTool
import io.quarkiverse.langchain4j.ModelName
import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.subscription.MultiEmitter
import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestQuery
import org.jboss.resteasy.reactive.RestStreamElementType
import org.testcontainers.shaded.org.hamcrest.MatcherAssert.assertThat
import java.io.File


@Path("/ai")
class AIResource(
    var chatModel: ChatLanguageModel,
    @ModelName("test") var ollamaModel: ChatLanguageModel,
    @ModelName("image") var openAiImageModel: ImageModel,
    @ModelName("test") var ollamaStreamingModel: StreamingChatLanguageModel,
    @ModelName("test") var embeddingModel: EmbeddingModel,
//    @Named("chroma") private val chromaStore: ChromaEmbeddingStore,
    @Named("milvus") private val milvusStore: MilvusEmbeddingStore,
    @Named("tcbMilvus") private val tcbMilvusStore: MilvusEmbeddingStore,
    @Named("scbMilvus") private val scbMilvusStore: MilvusEmbeddingStore,
    @Named("googleEngine") private val googleEngine: WebSearchEngine,
    var poemWriter: PoemWriter,
    var aiAssistant: AIAssistant,
    var garyProfileAssistant: GaryProfileAssistant,
    var tcbAssistant: TcbAssistant,
    var scbAssistant: SCBAssistant,
    var codeAssistant: CodeAssistant
) {

    private val logger = logger()

    @Inject
    lateinit var mailer: Mailer

    @GET
    @Path("/chatSimpleTest")
    fun chatSimpleTest(@RestQuery message: String?): String {
        return ollamaModel.generate(message)
    }

    @GET
    @Path("/chatStreamingTest")
    @RestStreamElementType(MediaType.TEXT_PLAIN)
    fun chatStreamingTest(@RestQuery message: String): Multi<String?> {
        return Multi.createFrom().emitter<String?> { emitter: MultiEmitter<in String?> ->
            ollamaStreamingModel.generate(
                message,
                object : StreamingResponseHandler<AiMessage> {
                    override fun onNext(token: String) {
                        emitter.emit(token)
                    }

                    override fun onError(error: Throwable) {
                        emitter.fail(error)
                    }

                    override fun onComplete(response: Response<AiMessage?>?) {
                        emitter.complete()
                    }
                })
        }
    }

    @GET
    @Path("/chatAssistantTest")
    fun chatAssistantTest(@RestQuery topic: String, @RestQuery lines: Int): String {
        return poemWriter.writeAPoem(topic, lines)
    }

    @GET
    @Path("/chatMemoryTest")
    fun chatMemoryTest(@RestQuery memoryId: String, @RestQuery message: String): String {
        return aiAssistant.chat(memoryId, message)
    }

    @GET
    @Path("/easyRagTest")
    fun easyRagTest(@RestQuery memoryId: String, @RestQuery message: String): String {
        return garyProfileAssistant.chat(memoryId, message)
    }

    @POST
    @Path("/storeDocFiles")
    @Consumes(MediaType.APPLICATION_JSON)
    fun storeDocFiles(@RestQuery fileDirPath: String, @RestQuery company: Company): String {
        logger.info("insertDocFiles")
        return run {
            when (company) {
                Company.SCB -> scbMilvusStore
                Company.TCB -> tcbMilvusStore
            }
        }.run {
            saveDocuments(fileDirPath, this)
            "Success upload documents"
        }
    }

    @GET
    @Path("/tcbRagTest")
    fun webcommRagTest(@RestQuery message: String): String {
        var response = tcbAssistant.chat(message)
        return response.content().text()
    }

    @GET
    @Path("/scbRagTest")
    fun scbRagTest(@RestQuery message: String): String {
        var response = scbAssistant.chat(message)
        return response.content().text()
    }

    @GET
    @Path("/codeChatTest")
    fun codeChatTest(@RestQuery message: String): String = codeAssistant.chat(message).content().text()

    @GET
    @Produces("image/png")
    @Path("/imageTest")
    fun imageTest(@RestQuery desc: String): File {
        logger.info("imageTest")
        val image: Image = openAiImageModel.generate(desc).content()
        return File(image.url())
    }

    @GET
    @Path("/webSearchTest")
    fun webSearchTest(@RestQuery message: String): String {

        val webSearchTool = WebSearchTool.from(googleEngine)
        val tools = ToolSpecifications.toolSpecificationsFrom(webSearchTool)
        val messages: MutableList<ChatMessage> = ArrayList()
        val systemMessage: SystemMessage =
            SystemMessage.from("你是一位非常了解台灣的專家，會回答關於台灣的相關知識")
        messages.add(systemMessage)
        val userMessage: UserMessage = UserMessage.from(message)
        messages.add(userMessage)

        // when
        val aiMessage: AiMessage = chatModel.generate(messages, tools).content()

        messages.add(aiMessage)

        // when
        val strResult = webSearchTool.searchWeb(message)
        val toolExecutionResultMessage =
            ToolExecutionResultMessage.from(aiMessage.toolExecutionRequests()[0], strResult)
        messages.add(toolExecutionResultMessage)

        val finalResponse: AiMessage = chatModel.generate(messages).content()
        return finalResponse.text()
    }

    @GET
    @Path("/mailTest")
    fun mailTest() {
        mailer.send(
            Mail.withText(
                "zipe.daden@gmail.com",
                "Ahoy from Quarkus",
                "A simple email sent from a Quarkus application."
            )
        );
    }

    @GET
    @Path("/view")
    @Produces(MediaType.TEXT_HTML)
    fun getViewerPage(): String {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>WebSocket Chat</title>
            <style>
                #chat-messages {
                    height: 300px;
                    overflow-y: scroll;
                    border: 1px solid #ccc;
                    padding: 10px;
                    margin-bottom: 10px;
                }
            </style>
        </head>
        <body>
            <div id="chat-messages"></div>
            <input type="text" id="message-input" placeholder="輸入訊息...">
            <button onclick="sendMessage()">發送</button>
        
            <script>
                var socket = new WebSocket("ws://" + location.host + "/ai/chat");
                var chatMessages = document.getElementById("chat-messages");
                var messageInput = document.getElementById("message-input");
        
                socket.onmessage = function(event) {
                    var message = document.createElement("p");
                    message.textContent = event.data;
                    chatMessages.appendChild(message);
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                };
        
                function sendMessage() {
                    if (messageInput.value) {
                        socket.send(messageInput.value);
                        messageInput.value = "";
                    }
                }
        
                messageInput.addEventListener("keypress", function(event) {
                    if (event.key === "Enter") {
                        sendMessage();
                    }
                });
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    private fun saveDocuments(fileDirPath: String, vector: EmbeddingStore<TextSegment>) {

        // 加載 Document
        val document = loadDocuments(fileDirPath, ApacheTikaDocumentParser())

        // 1. 建立文件分割
        val splitter = DocumentSplitters.recursive(300, 20)

        // 2. 文件分割
        val segments = splitter.splitAll(document)

        // 3. 建立EmbeddingModel
        val embeddingModel: EmbeddingModel = embeddingModel

        // 4. 使用EmbeddingModel輸入
        val embeddings: List<Embedding> = embeddingModel.embedAll(segments).content()

        // 5. 建立Embedding並儲存至 Vector
        val embeddingStore: EmbeddingStore<TextSegment> = vector

        // 6. 儲存Embeddings和segments
        embeddingStore.addAll(embeddings, segments)

        // 1~6可以使用EmbeddingStoreIngestor整合起来
//        val ingestor = EmbeddingStoreIngestor.builder()
//            .documentSplitter(DocumentSplitters.recursive(300, 0))
//            .embeddingModel(ollamaEmbedding)
//            .embeddingStore(chromaStore)
//            .build()

    }

}
