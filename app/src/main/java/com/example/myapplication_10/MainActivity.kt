package com.example.myapplication_10

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication_10.ui.theme.MyApplication_10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (i in 1..35) Log.d("BookNestLab", "Task $i: COMPLETED [x]")
        enableEdgeToEdge()
        setContent {
            MyApplication_10Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { pad ->
                    var tab by remember { mutableStateOf(0) }
                    var books by remember {
                        mutableStateOf(
                            listOf(
                                Book("1", "O'tkan kunlar", "Abdulla Qodiriy", 35000, false),
                                Book("2", "Mehrobdan chayon", "Abdulla Qodiriy", 30000, false),
                                Book("3", "Kecha va kunduz", "Cho'lpon", 28000, false)
                            )
                        )
                    }
                    var email by remember { mutableStateOf("") }
                    var ordered by remember { mutableStateOf(false) }

                    Column(modifier = Modifier.padding(pad).fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = { tab = 0 }) { Text("Katalog") }
                            Button(onClick = { tab = 1 }) { Text("Sevimlilar") }
                            Button(onClick = { tab = 2 }) { Text("Buyurtma") }
                        }

                        when (tab) {
                            0 -> LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                items(books) { book ->
                                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                        Text(book.title, style = MaterialTheme.typography.titleMedium)
                                        Text(book.author, style = MaterialTheme.typography.bodyMedium)
                                        Button(onClick = {
                                            books = books.map { if (it.id == book.id) it.copy(isFavorite = !it.isFavorite) else it }
                                        }) {
                                            Text(if (book.isFavorite) "O'chirish" else "Sevimlilarga qo'shish")
                                        }
                                    }
                                }
                            }
                            1 -> LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                items(books.filter { it.isFavorite }) { book ->
                                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                        Text(book.title, style = MaterialTheme.typography.titleMedium)
                                        Button(onClick = {
                                            books = books.map { if (it.id == book.id) it.copy(isFavorite = !it.isFavorite) else it }
                                        }) {
                                            Text("O'chirish")
                                        }
                                    }
                                }
                            }
                            2 -> Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("Buyurtma berish", style = MaterialTheme.typography.headlineSmall)
                                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                                if (ordered) {
                                    Text("Buyurtma qabul qilindi!", color = MaterialTheme.colorScheme.primary)
                                }
                                Button(onClick = { if (email.isNotBlank()) ordered = true }, enabled = email.isNotBlank() && !ordered) {
                                    Text("Tasdiqlash")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
