package io.avasia.projecttrellis

import io.avasia.projecttrellis.config.DotEnv
import io.avasia.projecttrellis.trello.TrelloCli

fun main(args: Array<String>) {
    val env = DotEnv.load()

    when (args.firstOrNull()) {
        "trello" -> TrelloCli(env).run(args.drop(1))
        null, "help", "--help", "-h" -> printHelp()
        else -> {
            println("Unknown command: ${args.first()}")
            printHelp()
        }
    }
}

private fun printHelp() {
    println(
        """
        ProjectTrellis

        Commands:
          trello boards
          trello lists [boardId]
          trello create-list <name> [boardId]
          trello create-card <listId> <title> [description]
          trello move-card <cardId> <listId>
          trello comment <cardId> <comment>
          trello bootstrap-board [boardId]

        Configuration:
          Copy .env.example to tokens.env and fill in TRELLO_KEY, TRELLO_TOKEN, and TRELLO_BOARD_ID.
        """.trimIndent()
    )
}
