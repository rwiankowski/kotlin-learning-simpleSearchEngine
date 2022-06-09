package search

fun main() {

    val input = readln()
    val searchFor = readln()

    val inputList = input.split(" ").map { it }.toMutableList()

    when(val searchResult = inputList.indexOf(searchFor)) {
        -1 -> println("Not Found")
        else -> println(searchResult + 1)
    }
}
