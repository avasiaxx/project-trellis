package io.avasia.projecttrellis.trello

import io.avasia.projecttrellis.config.DotEnv

class TrelloCli(env: DotEnv) {
    private val client by lazy {
        TrelloClient(
            key = env.require("TRELLO_KEY"),
            token = env.require("TRELLO_TOKEN"),
        )
    }
    private val defaultBoardId = env.get("TRELLO_BOARD_ID")

    fun run(args: List<String>) {
        when (args.firstOrNull()) {
            "boards" -> println(client.getBoards())
            "lists" -> println(client.getBoardLists(args.getOrNull(1) ?: requireBoardId()))
            "create-list" -> createList(args)
            "create-card" -> createCard(args)
            "move-card" -> moveCard(args)
            "comment" -> addComment(args)
            "bootstrap-board" -> bootstrapBoard(args.getOrNull(1) ?: requireBoardId())
            null, "help", "--help", "-h" -> printHelp()
            else -> {
                println("Unknown Trello command: ${args.first()}")
                printHelp()
            }
        }
    }

    private fun createList(args: List<String>) {
        val name = args.getOrNull(1) ?: error("Usage: trello create-list <name> [boardId]")
        val boardId = args.getOrNull(2) ?: requireBoardId()
        println(client.createList(boardId = boardId, name = name))
    }

    private fun createCard(args: List<String>) {
        val listId = args.getOrNull(1) ?: error("Usage: trello create-card <listId> <title> [description]")
        val title = args.getOrNull(2) ?: error("Usage: trello create-card <listId> <title> [description]")
        val description = args.drop(3).joinToString(" ")
        println(client.createCard(listId = listId, name = title, description = description))
    }

    private fun moveCard(args: List<String>) {
        val cardId = args.getOrNull(1) ?: error("Usage: trello move-card <cardId> <listId>")
        val listId = args.getOrNull(2) ?: error("Usage: trello move-card <cardId> <listId>")
        println(client.moveCard(cardId = cardId, listId = listId))
    }

    private fun addComment(args: List<String>) {
        val cardId = args.getOrNull(1) ?: error("Usage: trello comment <cardId> <comment>")
        val comment = args.drop(2).joinToString(" ").takeIf { it.isNotBlank() }
            ?: error("Usage: trello comment <cardId> <comment>")
        println(client.addComment(cardId = cardId, comment = comment))
    }

    private fun bootstrapBoard(boardId: String) {
        println("Creating board lists and labels. This command is not idempotent.")

        val lists = listOf("Roadmap / Decisions", "Backlog", "Ready", "Building", "Review", "Done")
            .associateWith { listName ->
                val response = client.createList(boardId = boardId, name = listName)
                response.extractTrelloId()
            }

        val labels = listOf(
            "Foundation" to "blue",
            "Database" to "green",
            "Desktop App" to "purple",
            "Website" to "sky",
            "Interactive Map" to "lime",
            "Design" to "pink",
            "Auth" to "red",
            "Sync" to "yellow",
            "Content Model" to "orange",
            "GitHub" to "black",
        ).associate { (name, color) ->
            val response = client.createLabel(boardId = boardId, name = name, color = color)
            name to response.extractTrelloId()
        }

        val foundationLabel = labels.getValue("Foundation")
        val githubLabel = labels.getValue("GitHub")
        val contentLabel = labels.getValue("Content Model")

        client.createCard(
            listId = lists.getValue("Roadmap / Decisions"),
            name = "Phase 1: Foundation",
            description = "Define the shape of the system before building major UI.",
            labelIds = listOf(foundationLabel),
        )
        client.createCard(
            listId = lists.getValue("Backlog"),
            name = "Review project inputs",
            description = "Inspect planning materials and map existing work into the first board model.",
            labelIds = listOf(contentLabel),
        )
        client.createCard(
            listId = lists.getValue("Backlog"),
            name = "Add Trello API helper for assisted updates",
            description = "Use local tokens.env credentials so project tooling can update the board after confirmation.",
            labelIds = listOf(foundationLabel),
        )
        client.createCard(
            listId = lists.getValue("Done"),
            name = "Set up repository",
            description = "Repository and normal push flow are working.",
            labelIds = listOf(githubLabel),
        )

        println("Bootstrap complete.")
        lists.forEach { (name, id) -> println("TRELLO_LIST_${name.toEnvName()}=$id") }
    }

    private fun requireBoardId(): String =
        defaultBoardId?.takeIf { it.isNotBlank() }
            ?: error("Provide a boardId argument or set TRELLO_BOARD_ID in tokens.env")

    private fun printHelp() {
        println(
            """
            Trello commands:
              trello boards
              trello lists [boardId]
              trello create-list <name> [boardId]
              trello create-card <listId> <title> [description]
              trello move-card <cardId> <listId>
              trello comment <cardId> <comment>
              trello bootstrap-board [boardId]
            """.trimIndent()
        )
    }
}

private fun String.extractTrelloId(): String =
    Regex(""""id"\s*:\s*"([^"]+)"""")
        .find(this)
        ?.groupValues
        ?.get(1)
        ?: error("Could not extract Trello id from response: $this")

private fun String.toEnvName(): String =
    uppercase()
        .replace(Regex("[^A-Z0-9]+"), "_")
        .trim('_')
