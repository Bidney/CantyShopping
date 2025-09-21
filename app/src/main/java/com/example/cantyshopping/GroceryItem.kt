package io.github.bidney.cantyshopping

data class GroceryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    var isChecked: Boolean = false,
    var quantity: String = "1",
    var category: GroceryCategory = GroceryCategory.OTHER
)