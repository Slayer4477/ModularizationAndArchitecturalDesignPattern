package com.example.myapplication_10

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface BookRepository {
    fun books(): Flow<List<Book>>
    suspend fun toggle(id: String)
}

class InMemoryBookRepository : BookRepository {
    private val list = MutableStateFlow(
        listOf(
            Book("1", "O'tkan kunlar", "Abdulla Qodiriy", 35000),
            Book("2", "Mehrobdan chayon", "Abdulla Qodiriy", 30000),
            Book("3", "Kecha va kunduz", "Cho'lpon", 28000)
        )
    )

    override fun books(): Flow<List<Book>> = list

    override suspend fun toggle(id: String) {
        list.value = list.value.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
    }
}
