package ai.llm

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test


@QuarkusTest
class MailTest {

    @Inject
    lateinit var mailer: Mailer

    @Test
    fun testTextMail() {

        mailer.send(Mail.withText("zipe.daden@gmail.com", "A simple email from quarkus", "This is my body"))
    }
}
