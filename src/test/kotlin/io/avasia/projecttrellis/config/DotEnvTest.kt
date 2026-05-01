package io.avasia.projecttrellis.config

import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DotEnvTest {
    @Test
    fun `loads key value pairs and strips matching quotes`() {
        val file = createTempFile("project-trellis", ".env")
        try {
            file.writeText(
                """
                # ignored
                TRELLO_KEY=abc
                TRELLO_TOKEN="quoted-token"
                EMPTY=
                SINGLE='quoted'
                """.trimIndent(),
            )

            val env = DotEnv.load(file)

            assertEquals("abc", env.get("TRELLO_KEY"))
            assertEquals("quoted-token", env.get("TRELLO_TOKEN"))
            assertEquals("", env.get("EMPTY"))
            assertEquals("quoted", env.get("SINGLE"))
            assertNull(env.get("MISSING"))
        } finally {
            file.deleteIfExists()
        }
    }
}
