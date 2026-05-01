package io.avasia.projecttrellis.trello

import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets

class TrelloClient(
    private val key: String,
    private val token: String,
    private val baseUrl: String = "https://api.trello.com",
) {
    private val http = HttpClient.newHttpClient()

    fun getBoards(): String =
        send(
            method = "GET",
            path = "/1/members/me/boards",
            query = mapOf("fields" to "name,url"),
        )

    fun getBoardLists(boardId: String): String =
        send(
            method = "GET",
            path = "/1/boards/$boardId/lists",
            query = mapOf("fields" to "name"),
        )

    fun createList(boardId: String, name: String): String =
        send(
            method = "POST",
            path = "/1/lists",
            query = mapOf(
                "idBoard" to boardId,
                "name" to name,
                "pos" to "bottom",
            ),
        )

    fun createLabel(boardId: String, name: String, color: String): String =
        send(
            method = "POST",
            path = "/1/labels",
            query = mapOf(
                "idBoard" to boardId,
                "name" to name,
                "color" to color,
            ),
        )

    fun createCard(
        listId: String,
        name: String,
        description: String,
        labelIds: List<String> = emptyList(),
    ): String =
        send(
            method = "POST",
            path = "/1/cards",
            query = buildMap {
                put("idList", listId)
                put("name", name)
                put("desc", description)
                if (labelIds.isNotEmpty()) {
                    put("idLabels", labelIds.joinToString(","))
                }
            },
        )

    fun moveCard(cardId: String, listId: String): String =
        send(
            method = "PUT",
            path = "/1/cards/$cardId",
            query = mapOf("idList" to listId),
        )

    fun addComment(cardId: String, comment: String): String =
        send(
            method = "POST",
            path = "/1/cards/$cardId/actions/comments",
            query = mapOf("text" to comment),
        )

    private fun send(
        method: String,
        path: String,
        query: Map<String, String> = emptyMap(),
    ): String {
        val uri = URI.create("$baseUrl$path?${query.withAuth().toQueryString()}")
        val request = HttpRequest.newBuilder(uri)
            .header("Accept", "application/json")
            .method(method, HttpRequest.BodyPublishers.noBody())
            .build()
        val response = http.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() !in 200..299) {
            error("Trello API request failed (${response.statusCode()}): ${response.body()}")
        }

        return response.body()
    }

    private fun Map<String, String>.withAuth(): Map<String, String> =
        this + mapOf(
            "key" to key,
            "token" to token,
        )

    private fun Map<String, String>.toQueryString(): String =
        entries.joinToString("&") { (key, value) ->
            "${key.urlEncode()}=${value.urlEncode()}"
        }

    private fun String.urlEncode(): String =
        URLEncoder.encode(this, StandardCharsets.UTF_8)
}
