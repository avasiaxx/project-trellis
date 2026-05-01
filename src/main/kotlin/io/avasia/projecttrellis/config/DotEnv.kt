package io.avasia.projecttrellis.config

import java.nio.file.Files
import java.nio.file.Path

class DotEnv private constructor(
    private val values: Map<String, String>,
) {
    fun get(name: String): String? = values[name] ?: System.getenv(name)

    fun require(name: String): String =
        get(name)?.takeIf { it.isNotBlank() }
            ?: error("Missing required configuration value: $name")

    companion object {
        fun load(path: Path = Path.of("tokens.env")): DotEnv {
            if (!Files.exists(path)) {
                return DotEnv(emptyMap())
            }

            val values = Files.readAllLines(path)
                .map { it.trim() }
                .filter { it.isNotBlank() && !it.startsWith("#") }
                .mapNotNull { line ->
                    val separator = line.indexOf('=')
                    if (separator <= 0) {
                        null
                    } else {
                        val key = line.substring(0, separator).trim()
                        val value = line.substring(separator + 1).trim().trimMatchingQuotes()
                        key to value
                    }
                }
                .toMap()

            return DotEnv(values)
        }
    }
}

private fun String.trimMatchingQuotes(): String {
    if (length < 2) {
        return this
    }

    val first = first()
    val last = last()
    return if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
        substring(1, lastIndex)
    } else {
        this
    }
}
