package com.example.cantyshopping

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cantyshopping.ui.theme.YourShoppingListTheme
import kotlinx.coroutines.launch

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
            YourShoppingListTheme {
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
        exportFileLauncher.launch("shopping-list.json")
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
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

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

    // State for snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Function to update and save grocery items
    fun updateGroceryItems(newList: List<GroceryItem>) {
        groceryItems = newList
        repository.saveGroceryItems(newList)
    }

    // Clear confirmation dialog with MD3 styling
    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = { Text("Clear Shopping List") },
            text = {
                Text("Are you sure you want to remove all items from your shopping list?",
                    style = MaterialTheme.typography.bodyLarge)
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        updateGroceryItems(emptyList())
                        showClearConfirmation = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Shopping list cleared")
                        }
                    }
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    // Menu with import, export, and clear options
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More options"
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
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
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                        scope.launch {
                            snackbarHostState.showSnackbar("Added ${newItem.name}")
                        }
                    }
                },
                icon = { Icon(Icons.Default.Add, "Add Item") },
                text = { Text("Add Item") },
                expanded = newItemName.isNotBlank(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            // Empty state
            if (groceryItems.isEmpty()) {
                EmptyListPlaceholder(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
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
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    groupedItems.forEach { (category, items) ->
                        // Only show categories that have items
                        if (items.isNotEmpty()) {
                            // Category header
                            item {
                                CategoryHeader(category)
                            }

                            // Items in this category
                            items(
                                items = items,
                                key = { it.id } // Use stable ID for animations
                            ) { item ->
                                GroceryItemRow(
                                    item = item,
                                    onCheckChange = { isChecked ->
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        val updatedList = groceryItems.map {
                                            if (it.id == item.id) it.copy(isChecked = isChecked) else it
                                        }
                                        updateGroceryItems(updatedList)
                                    },
                                    onDelete = {
                                        val updatedList = groceryItems.filter { it.id != item.id }
                                        updateGroceryItems(updatedList)
                                        scope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Item deleted",
                                                actionLabel = "Undo",
                                                duration = SnackbarDuration.Short
                                            )
                                            if (result == SnackbarResult.ActionPerformed) {
                                                updateGroceryItems(groceryItems) // Restore the list
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Add new item section - now more compact with the FAB handling the add action
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    // Category selector
                    Box {
                        OutlinedButton(
                            onClick = { showCategoryDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
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

                    // Item name field
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Add grocery item") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                        )
                    )
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
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        },
        content = {
            val elevation by animateDpAsState(
                targetValue = if (item.isChecked) 0.dp else 2.dp,
                animationSpec = tween(durationMillis = 300),
                label = "Card elevation animation"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                colors = CardDefaults.cardColors(
                    containerColor = if (item.isChecked)
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = onCheckChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (item.isChecked)
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete item",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryHeader(category: GroceryCategory) {
    // Get category icon
    val categoryIcon: ImageVector = when (category) {
        GroceryCategory.PRODUCE -> Icons.Outlined.ShoppingCart
        GroceryCategory.DAIRY -> Icons.Outlined.ShoppingCart
        GroceryCategory.MEAT -> Icons.Outlined.ShoppingCart
        GroceryCategory.BAKERY -> Icons.Outlined.ShoppingCart
        GroceryCategory.FROZEN -> Icons.Outlined.ShoppingCart
        GroceryCategory.CANNED -> Icons.Outlined.ShoppingCart
        GroceryCategory.DRY_GOODS -> Icons.Outlined.ShoppingCart  // Changed from DRY to DRY_GOODS
        GroceryCategory.BEVERAGES -> Icons.Outlined.ShoppingCart
        GroceryCategory.SNACKS -> Icons.Outlined.ShoppingCart
        GroceryCategory.HOUSEHOLD -> Icons.Outlined.ShoppingCart
        GroceryCategory.PERSONAL_CARE -> Icons.Outlined.ShoppingCart  // Added missing category
        GroceryCategory.OTHER -> Icons.Outlined.ShoppingCart
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon in circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = category.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun EmptyListPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your shopping list is empty",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Items you add will appear here",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}