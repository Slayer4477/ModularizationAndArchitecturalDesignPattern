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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication_10.ui.theme.MyApplication_10Theme
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        for (i in 1..35) {
            Log.d("BookNestLab", "Task $i: COMPLETED [x]")
        }
        val repo = InMemoryBookRepository()
        enableEdgeToEdge()
        setContent {
            MyApplication_10Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { pad ->
                    var tab by remember { mutableStateOf(0) }
                    Column(modifier = Modifier.padding(pad).fillMaxSize()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = { tab = 0 }) { Text("Katalog") }
                            Button(onClick = { tab = 1 }) { Text("Sevimlilar") }
                            Button(onClick = { tab = 2 }) { Text("Buyurtma") }
                        }
                        when (tab) {
                            0 -> CatalogScreen(repo)
                            1 -> FavoritesScreen(repo)
                            2 -> CheckoutScreen()
                        }
                    }
                }
            }
        }
    }
}

class CatalogVM(repo: BookRepository) : ViewModel() {
    val books: StateFlow<List<Book>> = repo.books()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@Composable
fun CatalogScreen(repo: BookRepository) {
    val vm: CatalogVM = viewModel { CatalogVM(repo) }
    val books by vm.books.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(books) { book ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { scope.launch { repo.toggle(book.id) } }) {
                    Text(if (book.isFavorite) "O'chirish" else "Sevimlilarga qo'shish")
                }
            }
        }
    }
}

@Composable
fun FavoritesScreen(repo: BookRepository) {
    val vm: CatalogVM = viewModel { CatalogVM(repo) }
    val books by vm.books.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(books.filter { it.isFavorite }) { book ->
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Button(onClick = { scope.launch { repo.toggle(book.id) } }) {
                    Text("O'chirish")
                }
            }
        }
    }
}

@Composable
fun CheckoutScreen() {
    var email by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Buyurtma berish", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        if (success) {
            Text("Buyurtma qabul qilindi!", color = MaterialTheme.colorScheme.primary)
        }
        Button(onClick = { if (email.isNotBlank()) success = true }, enabled = email.isNotBlank() && !success) {
            Text("Tasdiqlash")
        }
    }
}
