package search

import java.io.File

enum class Strategy {
    ANY, ALL, NONE
}

fun isStrategy(strategy: String): Boolean {
    for (enum in Strategy.values()) if (enum.name == strategy) return true
    return false
}

fun main(args: Array<String>) {

    val database = mutableListOf<String>()
    val index = mutableMapOf<String, MutableList<Int>>()

    populateData(database, args[1])
    indexData(database, index)

    do {
        println("=== Menu ===\n" +
                "1. Find a person\n" +
                "2. Print all people\n" +
                "0. Exit")

        when(readln().toInt()) {
            1 -> queryData(database, index)
            2 -> printData(database)
            0 -> break
            else -> println("Incorrect option! Try again.")
        }
    } while(true)

}
fun populateData(database: MutableList<String>, fileName: String) {

    File(fileName).forEachLine {
        database.add(it)
    }
}

fun indexData(database: MutableList<String>, index: MutableMap<String, MutableList<Int>>) {

    for (row in database) {
        val words = row.split(" ").map { it.lowercase() }.toList()

        for (word in words) {
            if (index.containsKey(word)) {
                val indexes = index.getValue(word)
                indexes.add(database.indexOf(row))
            } else {
                index[word] = mutableListOf(database.indexOf(row))
            }
        }
    }

}

fun queryData(database: MutableList<String>, index: MutableMap<String, MutableList<Int>>) {

    val results = mutableListOf<String>()
    var strategy: String

    do {
        println("Select a matching strategy: ALL, ANY, NONE")
        strategy = readln().uppercase()
    } while (!isStrategy(strategy))

    println("Provide the string to find:")
    val keyWords = readln().lowercase().split(" ").map { it }.toMutableList()

    when(strategy) {
        "ALL" -> {
            val indexes = mutableListOf<Int>()
            if (index.containsKey(keyWords[0])) indexes.addAll(index.getValue(keyWords[0]))

            keyWords.forEach { keyword ->
                if (index.containsKey(keyword)) index.getValue(keyword).forEach { innerIndex ->
                        if (!indexes.contains(innerIndex)) indexes.remove(innerIndex)
                }
            }
            indexes.forEach { item ->
                results.add(database[item])
            }
        }
        "ANY" -> {
            keyWords.forEach { keyword ->
                if (index.containsKey(keyword)) index.getValue(keyword).forEach { item ->
                    results.add(database[item])
                }
            }
        }
        "NONE" -> {
            val indexes = (0..database.lastIndex).toMutableList()
            keyWords.forEach { keyword ->
                if (index.containsKey(keyword)) index.getValue(keyword).forEach {item ->
                    indexes.remove(item)}
            }
            indexes.forEach { item ->
                results.add(database[item]) }
        }
    }

    if (results.isNotEmpty()) {
        println("People found:")
        results.forEach {result ->
            println(result)
        }
    } else {
        println("No matching people found.")
    }
}

fun printData(database: MutableList<String>) {

    println("=== List of people ===")
    for (row in database) println(row)

}