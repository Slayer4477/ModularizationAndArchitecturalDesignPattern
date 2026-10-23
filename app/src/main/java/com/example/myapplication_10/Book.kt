package com.example.myapplication_10

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val price: Int,
    val isFavorite: Boolean = false
)
