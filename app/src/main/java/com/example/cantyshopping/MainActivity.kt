package io.github.bidney.cantyshopping

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.delay
import io.github.bidney.cantyshopping.ui.theme.YourShoppingListTheme
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
                    // Remember the current state and allow it to be updated
                    var termsAccepted by remember { mutableStateOf(hasAcceptedTerms()) }

                    // Check if terms have been accepted
                    if (!termsAccepted) {
                        TermsAcceptanceScreen(
                            onTermsAccepted = {
                                acceptTerms()
                                termsAccepted = true // Update state to trigger recomposition
                            },
                            onViewTerms = { startActivity(Intent(this@MainActivity, TermsActivity::class.java)) }
                        )
                    } else {
                        ShoppingListScreen(
                            repository = repository,
                            onExportList = { exportList() },
                            onImportList = { importList() }
                        )
                    }
                }
            }
        }
    }

    private fun hasAcceptedTerms(): Boolean {
        val prefs = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return prefs.getBoolean("terms_accepted", false)
    }

    private fun acceptTerms() {
        val prefs = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("terms_accepted", true).apply()
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

@Composable
fun TermsAcceptanceScreen(
    onTermsAccepted: () -> Unit,
    onViewTerms: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App logo/icon area
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = "App Icon",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Welcome text
        Text(
            text = "Welcome to Canty Shopping",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your simple grocery shopping list app",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Terms acceptance card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Terms of Service",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Before you start using the app, please review and accept our terms of service.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Read Full Terms button
                TextButton(
                    onClick = onViewTerms,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Read Full Terms of Service",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox and agreement text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "I agree to the Terms of Service",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Continue button
                Button(
                    onClick = onTermsAccepted,
                    enabled = isChecked,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continue to App")
                }
            }
        }
    }
}

// Rest of your existing composables remain unchanged...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    repository: GroceryRepository,
    onExportList: () -> Unit,
    onImportList: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current // Get focus manager at Composable level

    // State to hold our shopping list - initialize from repository
    var groceryItems by remember { mutableStateOf(repository.getGroceryItems()) }

    // State for new item text field
    var newItemName by remember { mutableStateOf("") }

    // State for category selection
    var selectedCategory by remember { mutableStateOf(GroceryCategory.OTHER) }
    var showCategoryDropdown by remember { mutableStateOf(false) }

    // Track the newly added item for highlighting
    var highlightedItemId by remember { mutableStateOf<String?>(null) }

    // List state for scroll control
    val listState = rememberLazyListState()

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

    // Function to find position and scroll to a specific item
    suspend fun findPositionAndScrollToItem(
        items: List<GroceryItem>,
        itemId: String,
        listState: LazyListState
    ) {
        // Find the target item
        val targetItem = items.find { it.id == itemId } ?: return

        // Group by category to understand list structure
        val groupedItems = items.groupBy { it.category }
            .mapValues { (_, categoryItems) ->
                categoryItems.sortedBy { it.isChecked }
            }
            .toSortedMap(compareBy { it.ordinal })

        // Calculate index in the LazyColumn (including headers)
        var index = 0

        // Iterate through categories that come before our target
        for ((category, categoryItems) in groupedItems) {
            if (categoryItems.isEmpty()) continue

            // Add 1 for category header
            index++

            if (category == targetItem.category) {
                // Find position within this category
                for (item in categoryItems) {
                    if (item.id == itemId) {
                        // Found it! Scroll to this index with better positioning
                        listState.animateScrollToItem(
                            index = index,
                            scrollOffset = 100 // Position item with some space at the top
                        )
                        return
                    }
                    index++
                }
            } else {
                // Skip all items in this category
                index += categoryItems.size
            }
        }
    }

    // Function to add a new grocery item
    fun addGroceryItem(itemName: String, category: GroceryCategory? = null) {
        if (itemName.isBlank()) return

        // Auto-categorize if no category specified
        val itemCategory = category ?: GrocerySuggestions.guessCategory(itemName)

        val newItem = GroceryItem(
            name = itemName.trim(),
            category = itemCategory
        )

        // Add item to the list
        updateGroceryItems(groceryItems + newItem)
        newItemName = "" // Clear input field
        selectedCategory = GroceryCategory.OTHER // Reset category
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        // Set the highlighted item ID
        highlightedItemId = newItem.id

        // Improved focus behavior:
        scope.launch {
            // Give time for the list to update with the new item
            delay(100)

            // Find position and scroll to the new item - with better alignment
            findPositionAndScrollToItem(groceryItems, newItem.id, listState)

            // Keep the highlight a bit longer to ensure user sees it
            delay(2500) // 2.5 seconds instead of 2
            highlightedItemId = null
        }

        // Clear keyboard focus
        focusManager.clearFocus()

        scope.launch {
            snackbarHostState.showSnackbar("Added ${newItem.name}")
        }
    }

    // Function for item deletion with proper undo
    fun deleteGroceryItem(itemToDelete: GroceryItem) {
        // Save the current list before deletion for undo functionality
        val originalList = groceryItems.toList()

        // Remove the item from the list
        val updatedList = groceryItems.filter { it.id != itemToDelete.id }
        updateGroceryItems(updatedList)

        // Show snackbar with undo option
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = "Item deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                // Restore the original list that includes the deleted item
                updateGroceryItems(originalList)
            }
        }
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
                        text = "Your Shopping List",
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            // Add item input at the top
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
                        ),
                        trailingIcon = {
                            if (newItemName.isNotBlank()) {
                                IconButton(onClick = { addGroceryItem(newItemName, selectedCategory) }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Item",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    )

                    // Suggestions row
                    val suggestions = remember(newItemName, groceryItems) {
                        GrocerySuggestions.generateSuggestions(newItemName, groceryItems)
                    }

                    if (suggestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(suggestions) { suggestion ->
                                SuggestionChip(
                                    onClick = { addGroceryItem(suggestion) },
                                    label = { Text(suggestion) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Empty state
            if (groceryItems.isEmpty()) {
                EmptyListPlaceholder(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                // Group items by category and always sort within each category
                val groupedItems = groceryItems.groupBy { it.category }
                    .mapValues { (_, items) ->
                        items.sortedBy { it.isChecked }
                    }
                    .toSortedMap(compareBy { it.ordinal })

                // List of grocery items grouped by category
                LazyColumn(
                    state = listState,
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
                                // Add highlight animation for newly added items
                                val isHighlighted = item.id == highlightedItemId
                                GroceryItemRow(
                                    item = item,
                                    isHighlighted = isHighlighted,
                                    onCheckChange = { isChecked ->
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        val updatedList = groceryItems.map {
                                            if (it.id == item.id) it.copy(isChecked = isChecked) else it
                                        }
                                        updateGroceryItems(updatedList)
                                    },
                                    onDelete = {
                                        deleteGroceryItem(item)
                                    }
                                )
                            }
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
    isHighlighted: Boolean = false,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    // Only enable swipe-to-dismiss for unchecked items
    if (item.isChecked) {
        // For checked items, use a simpler card without swipe behavior
        CheckedItemCard(item, isHighlighted, onCheckChange, onDelete)
    } else {
        // For unchecked items, use swipe-to-dismiss
        SwipeableItemCard(item, isHighlighted, onCheckChange, onDelete)
    }
}

@Composable
private fun CheckedItemCard(
    item: GroceryItem,
    isHighlighted: Boolean,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    // Enhanced animation for highlighting
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 500),
        label = "Background color animation"
    )

    // Add a subtle elevation animation when highlighted
    val elevation by animateDpAsState(
        targetValue = if (isHighlighted) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "Elevation animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = true, // Always checked in this component
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
                textDecoration = TextDecoration.LineThrough,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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

@Composable
private fun SwipeableItemCard(
    item: GroceryItem,
    isHighlighted: Boolean,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    // Enhanced animation for highlighting
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 500),
        label = "Background color animation"
    )

    // Add a subtle elevation animation when highlighted
    val elevation by animateDpAsState(
        targetValue = if (isHighlighted) 8.dp else 2.dp,
        animationSpec = tween(durationMillis = 500),
        label = "Elevation animation"
    )

    // Simple card without swipe functionality
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false, // Always unchecked in this component
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
                color = MaterialTheme.colorScheme.onSurface
            )

            // Keep the delete button
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

@Composable
fun CategoryHeader(category: GroceryCategory) {
    // Get category icon
    val categoryIcon: ImageVector = when (category) {
        GroceryCategory.PRODUCE -> Icons.Outlined.ShoppingCart
        GroceryCategory.DAIRY -> Icons.Outlined.ShoppingCart
        GroceryCategory.DELI -> Icons.Outlined.ShoppingCart  // Add this line for the new DELI category
        GroceryCategory.MEAT -> Icons.Outlined.ShoppingCart
        GroceryCategory.BAKERY -> Icons.Outlined.ShoppingCart
        GroceryCategory.FROZEN -> Icons.Outlined.ShoppingCart
        GroceryCategory.CANNED -> Icons.Outlined.ShoppingCart
        GroceryCategory.DRY_GOODS -> Icons.Outlined.ShoppingCart
        GroceryCategory.BEVERAGES -> Icons.Outlined.ShoppingCart
        GroceryCategory.SNACKS -> Icons.Outlined.ShoppingCart
        GroceryCategory.HOUSEHOLD -> Icons.Outlined.ShoppingCart
        GroceryCategory.PERSONAL_CARE -> Icons.Outlined.ShoppingCart
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