package com.example.cantyshopping

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload // For Import
import androidx.compose.material.icons.filled.FileUpload // For Export
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.cantyshopping.ui.theme.CantyShoppingTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private lateinit var repository: GroceryRepository

    // Register activity result launchers for file operations
    private val exportFileLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { exportGroceryList(it) }
    }

    private val importFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let { importGroceryList(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize repository
        repository = GroceryRepository(applicationContext)

        setContent {
            CantyShoppingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListScreen(
                        repository = repository,
                        onExportList = { exportList() },
                        onImportList = { importList() }
                    )
                }
            }
        }
    }

    private fun exportList() {
        exportFileLauncher.launch("cantyshopping-list.json")
    }

    private fun importList() {
        importFileLauncher.launch(arrayOf("application/json"))
    }

    private fun exportGroceryList(uri: Uri) {
        try {
            val jsonString = repository.getGroceryItemsAsJson()
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
            Toast.makeText(this, "List exported successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to export list: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun importGroceryList(uri: Uri) {
        try {
            val jsonString = contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() ?: "" }
            if (jsonString.isNotEmpty()) {
                repository.loadGroceryItemsFromJson(jsonString)
                // Refresh the UI
                recreate()
                Toast.makeText(this, "List imported successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to import list: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    repository: GroceryRepository,
    onExportList: () -> Unit,
    onImportList: () -> Unit
) {
    // State to hold our shopping list - initialize from repository
    var groceryItems by remember { mutableStateOf(repository.getGroceryItems()) }

    // State for new item text field
    var newItemName by remember { mutableStateOf("") }

    // State for category selection
    var selectedCategory by remember { mutableStateOf(GroceryCategory.OTHER) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // State for confirmation dialog
    var showClearConfirmation by remember { mutableStateOf(false) }

    // State for menu
    var showMenu by remember { mutableStateOf(false) }

    // Function to update and save grocery items
    fun updateGroceryItems(newList: List<GroceryItem>) {
        groceryItems = newList
        repository.saveGroceryItems(newList)
    }

    // Clear confirmation dialog
    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("Clear Shopping List") },
            text = { Text("Are you sure you want to remove all items from your shopping list?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        updateGroceryItems(emptyList())
                        showClearConfirmation = false
                    }
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CantyShopping") },
                actions = {
                    // Menu with import, export, and clear options
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Import List") },
                                onClick = {
                                    onImportList()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.FileDownload,
                                        contentDescription = "Import"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Export List") },
                                onClick = {
                                    onExportList()
                                    showMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.FileUpload,
                                        contentDescription = "Export"
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Clear List") },
                                onClick = {
                                    showMenu = false
                                    showClearConfirmation = true
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Clear"
                                    )
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Group items by category and always sort within each category
            // Unchecked items first, then checked items
            val groupedItems = groceryItems.groupBy { it.category }
                .mapValues { (_, items) ->
                    // Always sort so unchecked items are first
                    items.sortedBy { it.isChecked }
                }
                .toSortedMap(compareBy { it.ordinal })

            // List of grocery items grouped by category
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                groupedItems.forEach { (category, items) ->
                    // Only show categories that have items
                    if (items.isNotEmpty()) {
                        // Category header
                        item {
                            CategoryHeader(category)
                        }

                        // Items in this category
                        items(items) { item ->
                            GroceryItemRow(
                                item = item,
                                onCheckChange = { isChecked ->
                                    val updatedList = groceryItems.map {
                                        if (it.id == item.id) it.copy(isChecked = isChecked) else it
                                    }
                                    updateGroceryItems(updatedList)
                                },
                                onDelete = {
                                    val updatedList = groceryItems.filter { it.id != item.id }
                                    updateGroceryItems(updatedList)
                                }
                            )
                        }
                    }
                }
            }

            // Add new item section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Category selector
                    Box {
                        OutlinedButton(
                            onClick = { showCategoryDropdown = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Category: ${selectedCategory.displayName}")
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            GroceryCategory.entries.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.displayName) },
                                    onClick = {
                                        selectedCategory = category
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Item name and add button
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newItemName,
                            onValueChange = { newItemName = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Add grocery item") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (newItemName.isNotBlank()) {
                                    // Auto-categorize if using default category
                                    val category = if (selectedCategory == GroceryCategory.OTHER) {
                                        guessCategory(newItemName)
                                    } else {
                                        selectedCategory
                                    }

                                    val newItem = GroceryItem(
                                        name = newItemName.trim(),
                                        category = category
                                    )
                                    updateGroceryItems(groceryItems + newItem)
                                    newItemName = ""
                                    selectedCategory = GroceryCategory.OTHER // Reset category
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add item")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryItemRow(
    item: GroceryItem,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onCheckChange
            )

            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            )

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CategoryHeader(category: GroceryCategory) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}